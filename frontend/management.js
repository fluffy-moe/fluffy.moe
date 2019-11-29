'use strict';
var radio_user_management_panel, radio_firebase_management_panel;

var div_user_management, div_firebase_management;

var users_area, btn_refresh_user;

var users_store, txt_search_user_field;

function init_management_page() {
	radio_user_management_panel = document.getElementById('radio_user_management_panel');
	radio_firebase_management_panel = document.getElementById('radio_firebase_management_panel');
	div_user_management = document.getElementById('div_user_management');
	div_firebase_management = document.getElementById('div_firebase_management');
	users_area = document.getElementById('users_area');
	btn_refresh_user = document.getElementById('btn_refresh_user');
	txt_search_user_field = document.getElementById('txt_search_user_field');

	btn_add_user.addEventListener('click', () => {
		window.location.replace(window.location.href + 'edit_user.html?new');
	});

	btn_refresh_user.addEventListener('click', () => {
		refresh_users();
	});

	radio_user_management_panel.addEventListener('click', () => {
		div_firebase_management.style.display = "none";
		div_user_management.style.display = "block";
	});

	radio_firebase_management_panel.addEventListener('click', () => {
		div_user_management.style.display = "none";
		div_firebase_device_id.style.display = "block";
	});

	txt_search_user_field.addEventListener('input', function() {
		update_users_list(this.value);
	});

	refresh_users();

}

function update_users_list(diff_value = '') {
	var _tmp = '<table><tr><th>Name</th><th>Phone</th></tr>';
	users_store.forEach(element => {
		if (diff_value !== '' && element.realname.indexOf(diff_value) === -1) 
			return;
		_tmp += '<tr><td><a href="/edit_user.html?' + element['id'] + '">'+ element['realname'] +
			'</a></td><td>'+ element['phone']+ '</td></tr>';
		
	});
	_tmp += '</table>';
	users_area.innerHTML = _tmp;
}

function refresh_users() {
	return $.getJSON('/request.php?t=user&' + new Date().getTime(), function (json_data) {
		if (json_data.data.length < 5)
			users_area.classList.remove("scrollable_area");
		else
			users_area.classList.add("scrollable_area");
		users_store = json_data.data;
		update_users_list();
	});
}
