// ============================================================
// HelpDesk — Global JS Utilities
// ============================================================

// ---- API Helper ----
const API = {
  async request(method, url, body) {
    const opts = {
      method,
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include'
    };
    if (body !== undefined) opts.body = JSON.stringify(body);
    const res = await fetch(url, opts);
    if (res.status === 401) { window.location.href = '/login.html'; return; }
    const data = await res.json().catch(() => null);
    if (!res.ok) throw new Error(data?.message || data?.error || `HTTP ${res.status}`);
    return data;
  },
  get:    (url)        => API.request('GET',    url),
  post:   (url, body)  => API.request('POST',   url, body),
  put:    (url, body)  => API.request('PUT',    url, body),
  patch:  (url, body)  => API.request('PATCH',  url, body),
  delete: (url)        => API.request('DELETE', url),
};

// ---- Auth Guard ----
async function requireAuth() {
  const user = await fetch('/auth/me', { credentials: 'include' })
    .then(r => r.ok ? r.json() : null).catch(() => null);
  if (!user) { window.location.href = '/login.html'; return null; }
  return user;
}

// ---- Build sidebar HTML and inject user info ----
function buildSidebar(user, activePage) {
  const isAdmin = user.role === 'ADMIN';
  const isAgent = user.role === 'AGENT' || isAdmin;

  const links = [
    { icon: 'bi-speedometer2', label: 'Dashboard', href: 'dashboard.html', page: 'dashboard', roles: ['ADMIN','AGENT','END_USER'] },
    { icon: 'bi-ticket-detailed', label: 'My Tickets', href: 'tickets.html', page: 'tickets', roles: ['ADMIN','AGENT','END_USER'] },
    { icon: 'bi-plus-circle', label: 'New Ticket', href: 'ticket-form.html', page: 'ticket-form', roles: ['ADMIN','AGENT','END_USER'] },
  ];
  const adminLinks = [
    { icon: 'bi-people', label: 'Users', href: 'users.html', page: 'users' },
    { icon: 'bi-person-check', label: 'Assignments', href: 'assignments.html', page: 'assignments' },
    { icon: 'bi-check2-circle', label: 'Resolutions', href: 'resolutions.html', page: 'resolutions' },
  ];

  const mainLinks = links
    .filter(l => l.roles.includes(user.role))
    .map(l => `<a href="${l.href}" class="nav-link ${activePage === l.page ? 'active' : ''}">
        <i class="bi ${l.icon}"></i>${l.label}</a>`).join('');

  const adminSection = isAdmin ? `
    <div class="nav-section">Admin</div>
    ${adminLinks.map(l => `<a href="${l.href}" class="nav-link ${activePage === l.page ? 'active' : ''}">
        <i class="bi ${l.icon}"></i>${l.label}</a>`).join('')}` : '';

  const sidebar = document.getElementById('sidebar');
  if (!sidebar) return;
  sidebar.innerHTML = `
    <div class="sidebar-brand"><i class="bi bi-headset"></i>HelpDesk</div>
    <nav class="mt-2 flex-grow-1">
      <div class="nav-section">Main</div>
      ${mainLinks}
      ${adminSection}
    </nav>
    <div class="sidebar-footer">
      <div class="d-flex align-items-center gap-2">
        <div class="bg-secondary rounded-circle d-flex align-items-center justify-content-center text-white"
             style="width:32px;height:32px;font-size:0.8rem;flex-shrink:0;">
          ${user.name.charAt(0).toUpperCase()}
        </div>
        <div class="overflow-hidden">
          <div class="text-white small fw-semibold text-truncate">${user.name}</div>
          <div class="text-muted" style="font-size:0.7rem;">${user.role}</div>
        </div>
        <a href="/auth/logout" class="ms-auto text-muted" title="Logout">
          <i class="bi bi-box-arrow-right"></i>
        </a>
      </div>
    </div>`;
}

// ---- Toast notification ----
function showToast(message, type = 'success') {
  let container = document.getElementById('toastContainer');
  if (!container) {
    container = document.createElement('div');
    container.id = 'toastContainer';
    container.className = 'toast-container position-fixed bottom-0 end-0 p-3';
    container.style.zIndex = '9999';
    document.body.appendChild(container);
  }
  const id = 'toast-' + Date.now();
  const icons = { success: 'bi-check-circle-fill', danger: 'bi-x-circle-fill', warning: 'bi-exclamation-triangle-fill' };
  container.insertAdjacentHTML('beforeend', `
    <div id="${id}" class="toast align-items-center text-bg-${type} border-0" role="alert">
      <div class="d-flex">
        <div class="toast-body"><i class="bi ${icons[type] || icons.success} me-2"></i>${message}</div>
        <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
      </div>
    </div>`);
  const el = document.getElementById(id);
  new bootstrap.Toast(el, { delay: 3500 }).show();
  el.addEventListener('hidden.bs.toast', () => el.remove());
}

// ---- Status / Priority badge renderers ----
function statusBadge(status) {
  const map = {
    OPEN:        ['badge-open',        'Open'],
    IN_PROGRESS: ['badge-in-progress', 'In Progress'],
    RESOLVED:    ['badge-resolved',    'Resolved'],
    CLOSED:      ['badge-closed',      'Closed'],
  };
  const [cls, label] = map[status] || ['badge-secondary', status];
  return `<span class="status-badge ${cls}">${label}</span>`;
}

function priorityBadge(priority) {
  const map = {
    LOW:      ['badge-low',      'Low'],
    MEDIUM:   ['badge-medium',   'Medium'],
    HIGH:     ['badge-high',     'High'],
    CRITICAL: ['badge-critical', 'Critical'],
  };
  const [cls, label] = map[priority] || ['badge-secondary', priority];
  return `<span class="priority-badge ${cls}">${label}</span>`;
}

// ---- Date formatter ----
function fmtDate(iso) {
  if (!iso) return '—';
  return new Date(iso).toLocaleString('en-IN', { dateStyle: 'medium', timeStyle: 'short' });
}

// ---- Sidebar mobile toggle ----
document.addEventListener('DOMContentLoaded', () => {
  const toggler = document.getElementById('sidebarToggler');
  if (toggler) {
    toggler.addEventListener('click', () => {
      document.getElementById('sidebar').classList.toggle('show');
    });
  }
});
