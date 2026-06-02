/* ============================================================
   QuizQuest front-end
   Plain JavaScript, no framework. Organised top-to-bottom:
   helpers -> state -> router -> one block per screen.
   ============================================================ */

// ---------- tiny helpers ----------
const $ = (sel, root = document) => root.querySelector(sel);
const $$ = (sel, root = document) => Array.from(root.querySelectorAll(sel));
const OPTION_KEYS = ["A", "B", "C", "D", "E", "F"];

function esc(s) {
  return String(s ?? "").replace(/[&<>"']/g, c =>
    ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;" }[c]));
}

function pad(n) { return String(n).padStart(2, "0"); }
function mmss(totalSeconds) {
  const s = Math.max(0, Math.round(totalSeconds));
  return `${pad(Math.floor(s / 60))}:${pad(s % 60)}`;
}

async function api(path, options = {}) {
  const res = await fetch(path, { headers: { "Content-Type": "application/json" }, ...options });
  if (!res.ok) {
    let message = `Error ${res.status}`;
    try { message = (await res.json()).message || message; } catch (e) { /* ignore */ }
    throw new Error(message);
  }
  return res.status === 204 ? null : res.json();
}

function toast(message, kind = "") {
  const el = document.createElement("div");
  el.className = `t ${kind}`;
  el.textContent = message;
  $("#toast").appendChild(el);
  setTimeout(() => el.remove(), 2800);
}

// ---------- app state ----------
const state = {
  meta: null,
  session: null,      // current live quiz
  answers: new Map(), // questionId -> selectedIndex
  flags: new Set(),   // flagged positions
  current: 0,
  timer: null,
  clockOffset: 0,     // serverNow - clientNow, to keep the countdown honest
  result: null,
};

// ---------- view switching ----------
function showView(name) {
  $$(".view").forEach(v => v.classList.remove("active"));
  const view = $(`#view-${name}`);
  if (view) view.classList.add("active");
  $$(".nav a").forEach(a => a.classList.toggle("active", a.dataset.view === name));
  window.scrollTo({ top: 0, behavior: "smooth" });
}

function go(hash) { window.location.hash = hash; }

function router() {
  const hash = window.location.hash || "#/";
  if (hash.startsWith("#/leaderboard")) return showLeaderboard();
  if (hash.startsWith("#/bank")) return showBank();
  if (hash.startsWith("#/quiz")) return state.session ? showView("quiz") : go("#/");
  if (hash.startsWith("#/result")) return state.result ? showView("result") : go("#/");
  return showHome();
}

// ============================================================
// HOME
// ============================================================
async function showHome() {
  showView("home");
  try {
    if (!state.meta) state.meta = await api("/api/meta");
    buildStartForm();
    const stats = await api("/api/stats");
    renderHomeStats(stats);
    renderTopBoard();
    renderRecent(stats.recent);
  } catch (e) {
    toast(e.message, "err");
  }
}

function buildStartForm() {
  const m = state.meta;
  const classSel = $("#f-class");
  if (!classSel.dataset.ready) {
    classSel.innerHTML = m.classLevels.map(c => `<option value="${c}">Class ${c}</option>`).join("");
    $("#f-subject").innerHTML =
      `<option value="">All subjects (mixed)</option>` +
      m.subjects.map(s => `<option value="${esc(s)}">${esc(s)}</option>`).join("");
    $("#f-count").innerHTML = m.suggestedCounts.map(n => `<option value="${n}">${n} questions</option>`).join("");

    $("#f-difficulty").innerHTML =
      `<button type="button" class="chip active" data-value="">All</button>` +
      m.difficulties.map(d =>
        `<button type="button" class="chip ${d.name.toLowerCase()}" data-value="${d.name}">${title(d.name)}</button>`).join("");
    $("#f-mode").innerHTML =
      `<button type="button" class="chip active" data-value="TIMED">⏱ Timed</button>` +
      `<button type="button" class="chip" data-value="PRACTICE">🧘 Practice</button>`;

    chipGroup("#f-difficulty");
    chipGroup("#f-mode");
    classSel.dataset.ready = "1";
  }
}

function chipGroup(sel) {
  $$(`${sel} .chip`).forEach(chip => chip.addEventListener("click", () => {
    $$(`${sel} .chip`).forEach(c => c.classList.remove("active"));
    chip.classList.add("active");
  }));
}
function chosenChip(sel) { return $(`${sel} .chip.active`)?.dataset.value ?? ""; }
function title(s) { return s.charAt(0) + s.slice(1).toLowerCase(); }

function renderHomeStats(stats) {
  $("#home-stats").innerHTML = `
    <div class="stat"><b>${stats.totalQuestions}</b><span>Questions</span></div>
    <div class="stat"><b>${stats.totalQuizzes}</b><span>Quizzes taken</span></div>
    <div class="stat"><b>${stats.topScore}</b><span>Top score</span></div>`;
}

async function renderTopBoard() {
  try {
    const board = await api("/api/leaderboard?limit=5");
    const ranks = ["gold", "silver", "bronze"];
    $("#home-board").innerHTML = board.length
      ? board.map((e, i) => `
        <li>
          <span class="rank ${ranks[i] || ""}">${e.rank}</span>
          <span class="who">${esc(e.studentName)} <small style="color:var(--muted)">· Cl ${e.classLevel}</small></span>
          <span class="pts">${e.score} pts</span>
        </li>`).join("")
      : `<li class="empty">No scores yet — be the first!</li>`;
  } catch (e) { /* non-critical */ }
}

function renderRecent(recent) {
  $("#home-recent").innerHTML = recent && recent.length
    ? recent.map(r => `
      <li>
        <span>${esc(r.studentName)} <span class="tag">· ${esc(r.subject || "Mixed")} · Cl ${r.classLevel}</span></span>
        <span><b>${r.correctCount}/${r.totalQuestions}</b> · ${r.score} pts</span>
      </li>`).join("")
    : `<li class="empty">No attempts yet.</li>`;
}

$("#start-form").addEventListener("submit", async (e) => {
  e.preventDefault();
  const btn = $("#start-form button[type=submit]");
  btn.disabled = true;
  try {
    const difficulty = chosenChip("#f-difficulty");
    const body = {
      studentName: $("#f-name").value.trim(),
      classLevel: Number($("#f-class").value),
      subject: $("#f-subject").value || null,
      difficulty: difficulty || null,
      count: Number($("#f-count").value),
      mode: chosenChip("#f-mode"),
    };
    const session = await api("/api/quizzes", { method: "POST", body: JSON.stringify(body) });
    startQuiz(session);
  } catch (err) {
    toast(err.message, "err");
  } finally {
    btn.disabled = false;
  }
});

// ============================================================
// QUIZ
// ============================================================
function startQuiz(session) {
  state.session = session;
  state.answers = new Map();
  state.flags = new Set();
  state.current = 0;
  state.clockOffset = Date.parse(session.serverNow) - Date.now();

  const diff = session.difficulty ? `<span class="pill ${session.difficulty.toLowerCase()}">${title(session.difficulty)}</span>` : `<span class="pill">Mixed</span>`;
  $("#quiz-meta").innerHTML = `
    <span class="pill">👤 ${esc(session.studentName)}</span>
    <span class="pill">Class ${session.classLevel}</span>
    <span class="pill">${esc(session.subject || "Mixed subjects")}</span>
    ${diff}
    <span class="pill">${session.mode === "TIMED" ? "⏱ Timed" : "🧘 Practice"}</span>`;

  renderPalette();
  renderQuestion();
  startTimer();
  go("#/quiz");
}

function renderQuestion() {
  const q = state.session.questions[state.current];
  $("#q-counter").textContent = `Question ${state.current + 1} of ${state.session.questions.length}`;
  $("#q-text").textContent = q.text;
  $("#flag-btn").classList.toggle("on", state.flags.has(state.current));

  const selected = state.answers.get(q.questionId);
  $("#q-options").innerHTML = q.options.map((opt, i) => `
    <button type="button" class="option ${selected === i ? "selected" : ""}" data-index="${i}">
      <span class="key">${OPTION_KEYS[i]}</span><span>${esc(opt)}</span>
    </button>`).join("");

  $$("#q-options .option").forEach(btn => btn.addEventListener("click", () => {
    state.answers.set(q.questionId, Number(btn.dataset.index));
    renderQuestion();
    renderPalette();
  }));

  $("#btn-prev").disabled = state.current === 0;
  $("#btn-next").disabled = state.current === state.session.questions.length - 1;
}

function renderPalette() {
  const total = state.session.questions.length;
  let html = "";
  for (let i = 0; i < total; i++) {
    const q = state.session.questions[i];
    const cls = [
      state.answers.has(q.questionId) ? "answered" : "",
      state.flags.has(i) ? "flagged" : "",
      i === state.current ? "current" : "",
    ].join(" ");
    html += `<button type="button" class="pal ${cls}" data-i="${i}">${i + 1}</button>`;
  }
  $("#q-palette").innerHTML = html;
  $$("#q-palette .pal").forEach(b => b.addEventListener("click", () => {
    state.current = Number(b.dataset.i);
    renderQuestion();
    renderPalette();
  }));
}

$("#btn-prev").addEventListener("click", () => { state.current--; renderQuestion(); renderPalette(); });
$("#btn-next").addEventListener("click", () => { state.current++; renderQuestion(); renderPalette(); });
$("#flag-btn").addEventListener("click", () => {
  if (state.flags.has(state.current)) state.flags.delete(state.current);
  else state.flags.add(state.current);
  renderQuestion();
  renderPalette();
});
$("#btn-submit").addEventListener("click", () => confirmSubmit());
$("#btn-submit-2").addEventListener("click", () => confirmSubmit());

function confirmSubmit() {
  const answered = state.answers.size;
  const total = state.session.questions.length;
  if (answered < total && !confirm(`You've answered ${answered} of ${total}. Submit anyway?`)) return;
  submitQuiz();
}

// ----- accurate, server-synced timer -----
const RING_CIRC = 2 * Math.PI * 34; // matches the SVG radius in the CSS

function startTimer() {
  stopTimer();
  const s = state.session;
  const deadline = Date.parse(s.deadline);
  const fill = $("#timer-fill");
  const timer = $("#quiz-timer");

  const tick = () => {
    const now = Date.now() + state.clockOffset;
    if (s.mode === "TIMED") {
      const remaining = (deadline - now) / 1000;
      $("#timer-label").textContent = mmss(remaining);
      const frac = Math.max(0, Math.min(1, remaining / s.durationSeconds));
      fill.style.strokeDashoffset = String(RING_CIRC * (1 - frac));
      timer.classList.toggle("low", remaining <= 10);
      if (remaining <= 0) {
        stopTimer();
        toast("Time's up! Submitting…", "err");
        submitQuiz();
      }
    } else {
      // practice: count time upward, keep the ring full
      const elapsed = (now - Date.parse(s.startedAt)) / 1000;
      $("#timer-label").textContent = mmss(elapsed);
      fill.style.strokeDashoffset = "0";
    }
  };
  tick();
  state.timer = setInterval(tick, 250);
}
function stopTimer() {
  if (state.timer) { clearInterval(state.timer); state.timer = null; }
}

async function submitQuiz() {
  stopTimer();
  const answers = state.session.questions.map(q => ({
    questionId: q.questionId,
    selectedIndex: state.answers.has(q.questionId) ? state.answers.get(q.questionId) : null,
  }));
  try {
    const result = await api(`/api/quizzes/${state.session.sessionId}/submit`,
      { method: "POST", body: JSON.stringify({ answers }) });
    state.result = result;
    state.session = null;
    renderResult(result);
    go("#/result");
  } catch (e) {
    toast(e.message, "err");
  }
}

// ============================================================
// RESULT
// ============================================================
function renderResult(r) {
  const pct = r.maxScore > 0 ? r.score / r.maxScore : 0;
  const dash = (1 - pct) * (2 * Math.PI * 56);
  const cheer = pct >= 0.8 ? "Brilliant! 🌟" : pct >= 0.5 ? "Well done! 👍" : "Keep practising! 💪";

  $("#result-summary").innerHTML = `
    <div class="score-ring">
      <svg viewBox="0 0 130 130">
        <defs><linearGradient id="rg" x1="0" y1="0" x2="1" y2="1">
          <stop offset="0" stop-color="#7c5cfc"/><stop offset="1" stop-color="#ff7eb6"/>
        </linearGradient></defs>
        <circle class="track" cx="65" cy="65" r="56"/>
        <circle class="fill" cx="65" cy="65" r="56"
          stroke-dasharray="${2 * Math.PI * 56}" stroke-dashoffset="${dash}"/>
      </svg>
      <div class="center"><b>${r.score}</b><span>of ${r.maxScore} pts</span></div>
    </div>
    <div class="result-head">
      <h2>${cheer}</h2>
      <p class="who">${esc(r.studentName)} · Class ${r.classLevel} · ${esc(r.subject || "Mixed")}
        <span class="badge-status ${r.status}">${r.status}</span></p>
      <div class="result-figs">
        <div class="fig"><b>${r.correctCount}/${r.totalQuestions}</b><span>Correct</span></div>
        <div class="fig"><b>${Math.round(r.accuracy * 100)}%</b><span>Accuracy</span></div>
        <div class="fig"><b>${mmss(r.elapsedSeconds)}</b><span>Time taken</span></div>
      </div>
    </div>`;

  $("#result-review").innerHTML = r.review.map(item => {
    const opts = item.options.map((opt, i) => {
      let cls = "";
      if (i === item.correctIndex) cls = "right";
      else if (i === item.selectedIndex) cls = "chosen-wrong";
      const mark = i === item.correctIndex ? " ✓" : (i === item.selectedIndex ? " ✗" : "");
      return `<div class="rev-opt ${cls}">${OPTION_KEYS[i]}. ${esc(opt)}${mark}</div>`;
    }).join("");
    const yourAnswer = item.selectedIndex == null ? "<i>not answered</i>" : `option ${OPTION_KEYS[item.selectedIndex]}`;
    return `
      <div class="rev-item ${item.correct ? "correct" : "wrong"}">
        <div class="rev-q">${item.position + 1}. ${esc(item.text)}</div>
        ${opts}
        <div class="rev-exp">You chose: ${yourAnswer}.${item.explanation ? ` <b>Why:</b> ${esc(item.explanation)}` : ""}</div>
      </div>`;
  }).join("");
}

$("#btn-again").addEventListener("click", () => go("#/"));

// ============================================================
// LEADERBOARD
// ============================================================
async function showLeaderboard() {
  showView("leaderboard");
  try {
    if (!state.meta) state.meta = await api("/api/meta");
    fillFilter("#lb-class", state.meta.classLevels.map(c => [String(c), `Class ${c}`]), "All classes");
    fillFilter("#lb-subject", state.meta.subjects.map(s => [s, s]), "All subjects");
    await loadLeaderboard();
  } catch (e) { toast(e.message, "err"); }
}

async function loadLeaderboard() {
  const params = new URLSearchParams({ limit: "20" });
  if ($("#lb-class").value) params.set("classLevel", $("#lb-class").value);
  if ($("#lb-subject").value) params.set("subject", $("#lb-subject").value);
  const rows = await api(`/api/leaderboard?${params}`);
  const medals = { 1: "🥇", 2: "🥈", 3: "🥉" };
  $("#lb-table").innerHTML = `
    <thead><tr><th>Rank</th><th>Student</th><th>Class</th><th>Subject</th><th>Level</th>
      <th>Score</th><th>Correct</th><th>Accuracy</th><th>Time</th></tr></thead>
    <tbody>${rows.length ? rows.map(r => `
      <tr>
        <td><span class="medal">${medals[r.rank] || r.rank}</span></td>
        <td><b>${esc(r.studentName)}</b></td>
        <td>${r.classLevel}</td>
        <td>${esc(r.subject || "Mixed")}</td>
        <td>${r.difficulty ? `<span class="diff-tag ${r.difficulty}">${r.difficulty}</span>` : "Mixed"}</td>
        <td><b>${r.score}</b></td>
        <td>${r.correctCount}/${r.totalQuestions}</td>
        <td>${Math.round(r.accuracy * 100)}%</td>
        <td>${mmss(r.elapsedSeconds)}</td>
      </tr>`).join("") : `<tr><td colspan="9" class="empty">No results yet.</td></tr>`}
    </tbody>`;
}

$("#lb-class").addEventListener("change", loadLeaderboard);
$("#lb-subject").addEventListener("change", loadLeaderboard);

// ============================================================
// QUESTION BANK (admin)
// ============================================================
async function showBank() {
  showView("bank");
  try {
    if (!state.meta) state.meta = await api("/api/meta");
    fillFilter("#bank-class", state.meta.classLevels.map(c => [String(c), `Class ${c}`]), "All classes");
    fillFilter("#bank-subject", state.meta.subjects.map(s => [s, s]), "All subjects");
    fillFilter("#bank-difficulty", state.meta.difficulties.map(d => [d.name, title(d.name)]), "All levels");
    await loadBank();
  } catch (e) { toast(e.message, "err"); }
}

async function loadBank() {
  const params = new URLSearchParams();
  if ($("#bank-class").value) params.set("classLevel", $("#bank-class").value);
  if ($("#bank-subject").value) params.set("subject", $("#bank-subject").value);
  if ($("#bank-difficulty").value) params.set("difficulty", $("#bank-difficulty").value);
  const rows = await api(`/api/questions?${params}`);
  $("#bank-table").innerHTML = `
    <thead><tr><th>Class</th><th>Subject</th><th>Level</th><th>Question</th><th>Answer</th><th></th></tr></thead>
    <tbody>${rows.length ? rows.map(q => `
      <tr>
        <td>${q.classLevel}</td>
        <td>${esc(q.subject)}</td>
        <td><span class="diff-tag ${q.difficulty}">${q.difficulty}</span></td>
        <td>${esc(q.text)}</td>
        <td>${esc(q.options[q.correctIndex] ?? "")}</td>
        <td><div class="row-actions">
          <button class="icon-btn" data-edit="${q.id}" title="Edit">✎</button>
          <button class="icon-btn danger" data-del="${q.id}" title="Delete">🗑</button>
        </div></td>
      </tr>`).join("") : `<tr><td colspan="6" class="empty">No questions match these filters.</td></tr>`}
    </tbody>`;
  $$("#bank-table [data-edit]").forEach(b => b.addEventListener("click", () => openModal(rows.find(q => q.id == b.dataset.edit))));
  $$("#bank-table [data-del]").forEach(b => b.addEventListener("click", () => deleteQuestion(b.dataset.del)));
}

["#bank-class", "#bank-subject", "#bank-difficulty"].forEach(s => $(s).addEventListener("change", loadBank));

async function deleteQuestion(id) {
  if (!confirm("Delete this question?")) return;
  try { await api(`/api/questions/${id}`, { method: "DELETE" }); toast("Question deleted", "ok"); loadBank(); }
  catch (e) { toast(e.message, "err"); }
}

// ----- add / edit modal -----
function openModal(question) {
  $("#modal-title").textContent = question ? "Edit question" : "Add question";
  $("#q-id").value = question ? question.id : "";
  $("#q-class").value = question ? question.classLevel : "";
  $("#q-subject-in").value = question ? question.subject : "";
  $("#q-difficulty-in").innerHTML = state.meta.difficulties.map(d =>
    `<option value="${d.name}">${title(d.name)} (${d.points} pt)</option>`).join("");
  $("#q-difficulty-in").value = question ? question.difficulty : "EASY";
  $("#q-text-in").value = question ? question.text : "";
  $("#q-explanation-in").value = question ? (question.explanation || "") : "";

  const options = question ? question.options : ["", "", "", ""];
  const correct = question ? question.correctIndex : 0;
  renderOptionInputs(options, correct);
  $("#modal").classList.add("open");
}

function renderOptionInputs(options, correctIndex) {
  $("#q-options-in").innerHTML = options.map((opt, i) => optionRow(opt, i, i === correctIndex)).join("");
  wireOptionRows();
}
function optionRow(value, i, checked) {
  return `<div class="opt-row">
    <input type="radio" name="correct" value="${i}" ${checked ? "checked" : ""} title="Correct answer" />
    <input type="text" class="opt-text" placeholder="Option ${OPTION_KEYS[i]}" value="${esc(value)}" maxlength="300" />
    <button type="button" class="del" title="Remove">✕</button>
  </div>`;
}
function wireOptionRows() {
  $$("#q-options-in .del").forEach(btn => btn.addEventListener("click", () => {
    const rows = $$("#q-options-in .opt-row");
    if (rows.length <= 2) return toast("Need at least two options", "err");
    btn.closest(".opt-row").remove();
    reindexOptionRows();
  }));
}
function reindexOptionRows() {
  $$("#q-options-in .opt-row").forEach((row, i) => {
    row.querySelector("input[type=radio]").value = String(i);
    row.querySelector(".opt-text").placeholder = `Option ${OPTION_KEYS[i]}`;
  });
}
$("#btn-add-option").addEventListener("click", () => {
  const rows = $$("#q-options-in .opt-row");
  if (rows.length >= 6) return toast("Up to six options", "err");
  $("#q-options-in").insertAdjacentHTML("beforeend", optionRow("", rows.length, false));
  wireOptionRows();
});

function closeModal() { $("#modal").classList.remove("open"); }
$("#modal-close").addEventListener("click", closeModal);
$("#btn-cancel").addEventListener("click", closeModal);
$("#modal").addEventListener("click", e => { if (e.target === $("#modal")) closeModal(); });
$("#btn-add").addEventListener("click", () => openModal(null));

$("#question-form").addEventListener("submit", async (e) => {
  e.preventDefault();
  const options = $$("#q-options-in .opt-text").map(i => i.value.trim()).filter(Boolean);
  const correctRadio = $("#q-options-in input[type=radio]:checked");
  if (options.length < 2) return toast("Add at least two non-empty options", "err");
  if (!correctRadio) return toast("Mark which option is correct", "err");
  const correctIndex = Number(correctRadio.value);
  if (correctIndex >= options.length) return toast("The correct option is empty", "err");

  const body = {
    classLevel: Number($("#q-class").value),
    subject: $("#q-subject-in").value.trim(),
    difficulty: $("#q-difficulty-in").value,
    text: $("#q-text-in").value.trim(),
    options,
    correctIndex,
    explanation: $("#q-explanation-in").value.trim(),
  };
  const id = $("#q-id").value;
  try {
    await api(id ? `/api/questions/${id}` : "/api/questions",
      { method: id ? "PUT" : "POST", body: JSON.stringify(body) });
    toast(id ? "Question updated" : "Question added", "ok");
    closeModal();
    state.meta = await api("/api/meta"); // subjects/classes may have changed
    loadBank();
  } catch (err) {
    toast(err.message, "err");
  }
});

// ---------- shared filter helper ----------
function fillFilter(sel, pairs, allLabel) {
  const el = $(sel);
  const current = el.value;
  el.innerHTML = `<option value="">${allLabel}</option>` +
    pairs.map(([v, label]) => `<option value="${esc(v)}">${esc(label)}</option>`).join("");
  el.value = current;
}

// ---------- boot ----------
window.addEventListener("hashchange", router);
window.addEventListener("DOMContentLoaded", async () => {
  try {
    state.meta = await api("/api/meta");
  } catch (e) {
    toast("Could not reach the server.", "err");
  }
  router();
  setTimeout(() => $("#loader").classList.add("hidden"), 250);
});
