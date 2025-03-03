// Highlight current navbar link on page reload
document.addEventListener('DOMContentLoaded', colorLink);

/*===== EXPANDER MENU  =====*/
const showMenu = (toggleId, navbarId) => {
    const toggle = document.getElementById(toggleId);
    const navbar = document.getElementById(navbarId);

    if (toggle && navbar) {
        toggle.addEventListener('click', () => {
            navbar.classList.toggle('expander');
        });
    }
}

showMenu('nav-toggle', 'navbar');

/*===== LINK ACTIVE  =====*/
const linkColor = document.querySelectorAll('.nav__link');

function colorLink() {
	const currentURL = window.location.pathname.split('/').pop();

    linkColor.forEach(link => {
		const linkURL = new URL(link.href).pathname.split('/').pop();

		if (linkURL === currentURL) {
			link.classList.add('active');
		} else {
			link.classList.remove('active');
		}
	});
}

linkColor.forEach(l => l.addEventListener('click', colorLink));

/*===== COLLAPSE MENU  =====*/
const linkCollapse = document.getElementsByClassName('collapse__link');

for (let i = 0; i < linkCollapse.length; i++) {
    linkCollapse[i].addEventListener('click', function () {
        const collapseMenu = this.nextElementSibling;
        collapseMenu.classList.toggle('showCollapse');

        const rotate = collapseMenu.previousElementSibling
        rotate.classList.toggle('rotate');
    });
}

// Close the sidebar when clicking outside of it
const closeSidebar = (event) => {
    const navbar = document.getElementById('navbar');
    const toggle = document.getElementById('nav-toggle');

    if (!navbar.contains(event.target) && !toggle.contains(event.target)) {
        navbar.classList.remove('expander');
    }
};

document.addEventListener('click', closeSidebar);