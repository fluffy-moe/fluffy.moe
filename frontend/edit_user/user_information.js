'use strict';
var txt_info_name, txt_info_email, txt_info_phone, txt_info_address,
	txt_info_animalname, rd_info_gender_male, rd_info_gender_female,
	txt_info_varity, txt_info_color, txt_info_picture, txt_info_birthday,
	txt_info_age, txt_info_weight, rd_info_neuter_yes, rd_info_neuter_no,
	rd_info_vac_have, rd_info_vac_nothave, txt_info_vacdate, txt_info_vacproduct,
	txt_info_vacsite, txt_info_vacdoctor, txt_info_vacnextdate, rd_info_dein_have,
	rd_info_dein_nothave, txt_info_desdate, txt_info_desproduct, txt_info_desdoctor,
	txt_info_desnextdate, txt_info_hospitaldate, txt_info_hospitalbackdate,
	txt_info_outpatientdate, txt_info_outpatient_nexttime, txt_info_symptom,
	txt_info_RBC, txt_info_HCT, txt_info_MCH, txt_info_MCHC, txt_info_CREA,
	txt_info_BUM, txt_info_PHOS, txt_info_CA, txt_info_ALB, txt_info_CHOL,
	txt_info_PCT, dropdown_pet_select;

var button_update_personal_information, button_update_pet_information, button_add_new_pet;

var table_vaccination_record, table_deinsectzation_record;

var rev_user_id;

function init_view() {
	txt_info_name = document.getElementById('info_name');
	txt_info_email = document.getElementById('info_email');
	txt_info_phone = document.getElementById('info_phone');
	txt_info_address = document.getElementById('info_address');
	txt_info_animalname = document.getElementById('info_animalname');
	rd_info_gender_male = document.getElementById('info_gender_male');
	rd_info_gender_female = document.getElementById('info_gender_female');
	txt_info_varity = document.getElementById('info_varity');
	txt_info_color = document.getElementById('info_color');
	txt_info_picture = document.getElementById('info_picture');
	txt_info_birthday = document.getElementById('info_birthday');
	txt_info_age = document.getElementById('info_age');
	txt_info_weight = document.getElementById('info_weight');
	rd_info_neuter_yes = document.getElementById('info_neuter_yes');
	rd_info_neuter_no = document.getElementById('info_neuter_no');
	rd_info_vac_have = document.getElementById('info_vac_have');
	rd_info_vac_nothave = document.getElementById('info_vac_nothave');
	txt_info_vacdate = document.getElementById('info_vacdate');
	txt_info_vacproduct = document.getElementById('info_vacproduct');
	txt_info_vacsite = document.getElementById('info_vacsite');
	txt_info_vacdoctor = document.getElementById('info_vacdoctor');
	txt_info_vacnextdate = document.getElementById('info_vacnextdate');
	rd_info_dein_have = document.getElementById('info_dein_have');
	rd_info_dein_nothave = document.getElementById('info_dein_nothave');
	txt_info_desdate = document.getElementById('info_desdate');
	txt_info_desproduct = document.getElementById('info_desproduct');
	txt_info_desdoctor = document.getElementById('info_desdoctor');
	txt_info_desnextdate = document.getElementById('info_desnextdate');
	txt_info_hospitaldate = document.getElementById('info_hospitaldate');
	txt_info_hospitalbackdate = document.getElementById('info_hospitalbackdate');
	txt_info_outpatientdate = document.getElementById('info_outpatientdate');
	txt_info_outpatient_nexttime = document.getElementById('info_outpatient_nexttime');
	txt_info_symptom = document.getElementById('info_symptom');
	txt_info_RBC = document.getElementById('info_RBC');
	txt_info_HCT = document.getElementById('info_HCT');
	txt_info_MCH = document.getElementById('info_MCH');
	txt_info_MCHC = document.getElementById('info_MCHC');
	txt_info_CREA = document.getElementById('info_CREA');
	txt_info_BUM = document.getElementById('info_BUM');
	txt_info_PHOS = document.getElementById('info_PHOS');
	txt_info_CA = document.getElementById('info_CA');
	txt_info_ALB = document.getElementById('info_ALB');
	txt_info_CHOL = document.getElementById('info_CHOL');
	txt_info_PCT = document.getElementById('info_PCT');
	button_update_personal_information = document.getElementById('btn_personal_information_update');
	button_update_pet_information = document.getElementById('btn_pets_information_update');
	button_add_new_pet = document.getElementById('btn_add_new_pet');
	dropdown_pet_select = document.getElementById('pets_select');
	table_vaccination_record = document.getElementById('table_vaccination_record');
	table_deinsectzation_record = document.getElementById('table_deinsectzation_record');
	rev_user_id = document.getElementById('rev_user_id');
	init_onclick();
}

function init_onclick() {
	button_update_personal_information.addEventListener('click', function () {
		do_POST('update_person', {
			name: txt_info_name.value,
			phone: txt_info_phone.value,
			email: txt_info_email.value,
			address: txt_info_address.value
		});
	});

	button_update_pet_information.addEventListener('click', function () {
		var gender = (rd_info_gender_male.checked ? 'M' : 'W');
		var is_neuter = (rd_info_neuter_yes.checked ? 'Y' : 'N');
		do_POST('update_pet', {
			pet_no: dropdown_pet_select.value,
			petname: txt_info_animalname.value,
			gender: gender,
			gender_female: rd_info_gender_female.value,
			varity: txt_info_varity.value,
			color: txt_info_color.value,
			//picture: txt_info_picture.value,
			birthday: txt_info_birthday.value,
			//age: txt_info_age.value,
			weight: txt_info_weight.value,
			neuter: is_neuter
		});
	});

	button_add_new_pet.addEventListener('click', () => {
		if (dropdown_pet_select.innerHTML.search('None') !== -1) {
			dropdown_pet_select.innerHTML = '';
			dropdown_pet_select.disabled = false;
		}
		if (dropdown_pet_select.innerHTML.search('new') === -1)
			dropdown_pet_select.innerHTML += '<option>new</option>';
		dropdown_pet_select.value = 'new';
		rd_info_gender_male.checked = false;
		rd_info_gender_female.checked = true;
		txt_info_varity.value = '';
		txt_info_color.value = '';
		rd_info_neuter_yes.checked = false;
		rd_info_neuter_no.checked = true;
		txt_info_birthday.value = '';
	});

	dropdown_pet_select.addEventListener('change', () => {
		console.log(this.value);
	});

	rd_info_vac_have.addEventListener('click', () => {
		table_vaccination_record.style.display = "block";
	});

	rd_info_vac_nothave.addEventListener('click', () => {
		table_vaccination_record.style.display = "none";
	});

	rd_info_dein_have.addEventListener('click', () => {
		table_deinsectzation_record.style.display = "block";
	});

	rd_info_dein_nothave.addEventListener('click', () => {
		table_deinsectzation_record.style.display = "none";
	});

	getUserInfo();
}

/**
 * NOTE: must check return type if you want to use return type
 */
function getUserInfo() {
	var user_id = findGetParameter('user');
	rev_user_id.value = user_id;
	console.log(user_id);
	var rt;
	if (user_id !== null) {
		rt = $.getJSON('/request.php?t=user_detail&user_id=' + user_id + '&' + new Date().getTime(), function(json_data) {
			console.log(json_data);
			var user_info = json_data.data.user;
			txt_info_name.value = user_info['realname'];
			txt_info_email.value = user_info['email'];
			txt_info_phone.value = user_info['phone'];
			txt_info_address.value = user_info['address'];
		});
	} else {
		rt = null;
	}
	return rt;
}

function do_POST(t, payload, callback = () => {}) {
	return $.post('/request.php', {
		t: t,
		payload: JSON.stringify(payload)
	}, callback);
}

/**
 * Reference: https://stackoverflow.com/a/5448595
 * 
 * @param {String} parameterName which want get from url
 */
function findGetParameter(parameterName) {
    var result = null,
        tmp = [];
    location.search
        .substr(1)
        .split("&")
        .forEach(function (item) {
          tmp = item.split("=");
          if (tmp[0] === parameterName) result = decodeURIComponent(tmp[1]);
        });
    return result;
}