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
	txt_info_PCT, dropdown_pet_select, select_vac_history, select_dei_history;

var button_update_personal_information, button_update_pet_information, button_add_new_pet;

var table_vaccination_record, table_deinsectzation_record;

var rev_user_id, rev_pet_id, rev_vac_id, rev_dein_id;
var user_id;

var pet_data_store;

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
	txt_info_CGB = document.getElementById('info_CGB');
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
	rev_pet_id = document.getElementById('rev_pet_id');
	rev_vac_id = document.getElementById('rev_vac_id');
	rev_dein_id = document.getElementById('rev_dein_id');
	select_vac_history = document.getElementById('select_vac_history');
	select_dei_history = document.getElementById('select_dei_history');
	init_onclick();
}

function reslove_result(result) {
	try {
		return result.options[0];
	} catch (ignore) {
		return JSON.parse(result).options[0];
	}
}

function init_onclick() {
	button_update_personal_information.addEventListener('click', function () {
		do_POST('update_person', {
			uid: rev_user_id.value,
			name: txt_info_name.value,
			phone: txt_info_phone.value,
			email: txt_info_email.value,
			address: txt_info_address.value
		}, (result) => {
			if (rev_user_id.value === '') // only new user shou check this option
				rev_user_id.value = reslove_result(result);
		});
	});

	button_update_pet_information.addEventListener('click', function () {
		var gender = (rd_info_gender_male.checked ? 'M' : 'W');
		var is_neuter = (rd_info_neuter_yes.checked ? 'Y' : 'N');
		if (rev_user_id.value === '') {
			return alert("please create user first!");
		}
		do_POST('update_pet', {
			belong: rev_user_id.value,
			pet_no: rev_pet_id.value,
			petname: txt_info_animalname.value,
			gender: gender,
			varity: txt_info_varity.value,
			color: txt_info_color.value,
			//picture: txt_info_picture.value,
			birthday: txt_info_birthday.value,
			//age: txt_info_age.value,
			weight: txt_info_weight.value,
			neuter: is_neuter
		}, (result) => {
			if (rev_pet_id.value === '') {
				console.log('test');
				rev_pet_id.value = reslove_result(result);
				dropdown_pet_select.innerHTML = dropdown_pet_select.innerHTML.replace('<option>new</option>',
					'<option value="'+ rev_pet_id.value +'">' + txt_info_animalname.value + '</option>');
				dropdown_pet_select.value = rev_pet_id.value;
			}
		});
	});

	button_add_new_pet.addEventListener('click', () => {
		if (dropdown_pet_select.innerHTML.search('None') !== -1) {
			dropdown_pet_select.innerHTML = '';
			dropdown_pet_select.disabled = false;
			button_update_pet_information.disabled = false;
		}
		if (dropdown_pet_select.innerHTML.search('<option>new</option>') === -1)
			dropdown_pet_select.innerHTML += '<option>new</option>';
		rev_pet_id.value = '';
		dropdown_pet_select.value = 'new';
		txt_info_animalname.value = '';
		txt_info_weight.value = '';
		rd_info_gender_male.checked = false;
		rd_info_gender_female.checked = true;
		txt_info_varity.value = '';
		txt_info_color.value = '';
		rd_info_neuter_yes.checked = false;
		rd_info_neuter_no.checked = true;
		txt_info_birthday.value = '';
	});

	dropdown_pet_select.addEventListener('change', () => {
		update_website_pet_info(pet_data_store[dropdown_pet_select.value]);
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

	select_vac_history.addEventListener('change', () => {
		update_vac_info(pet_data_store[dropdown_pet_select.value].vaccination[select_vac_history.value]);
	});

	select_dei_history.addEventListener('change', () => {
		update_dei_info(pet_data_store[dropdown_pet_select.value].deinsectzation[select_dei_history.value]);
	});

	$('btn_update_vac_info').click(() => {
		do_POST('update_vac', {
			id: rev_vac_id.value,
			date: txt_info_vacdate.value,
			product: txt_info_vacproduct.value,
			injection_site: txt_info_vacsite.value,
			doctor: txt_info_vacdoctor.value
		});
	});

	$('btn_update_dein_info').click(() => {
		do_POST('update_dein', {
			date: txt_info_desdate.value,
			product: txt_info_desproduct.value,
			doctor: txt_info_desdoctor.value
		});
	});

	$('btn_update_hs_info').click(() => {
        do_POST('update_hs',{
			start_date: txt_info_hospitaldate.value,
			end_date: txt_info_hospitalbackdate.value
		});
	});

	$('btn_update_opc_info').click(() => {
        do_POST('update_opc',{
			date: txt_info_outpatientdate.value,
			symptom: txt_info_symptom.value
		});
	});

    $('btn_update_hem_info').click(() => {
        do_POST('update_hem',{
			RBC: txt_info_RBC.value,
			HCT: txt_info_HCT.value,
			CGB: txt_info_CBG.value,
			MCH: txt_info_MCH.value,
			MCHC: txt_info_MCHC.value
		});
	});

	$('btn_update_kid_info').click(() => {
        do_POST('update_kid',{
			CREA: txt_info_CREA.value,
			BUM: txt_info_BUM.value,
			PHOS: txt_info_PHOS.value,
			CA: txt_info_CA.value,
			ALB: txt_info_ALB.value,
			CHOL: txt_info_CHOL.value,
			PCT: txt_info_PCT.value,
		});
	});

	get_user_info();
}

/**
 * NOTE: must check return type if you want to use return type
 */
function get_user_info() {
	user_id = findGetParameter('user');
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
		get_pet_info();
	} else {
		rt = null;
	}
	return rt;
}


function toggle_vac_info(is_exist){
	rd_info_vac_have.checked = is_exist;
	rd_info_vac_nothave.checked = !is_exist;
	table_vaccination_record.style.display = (is_exist?"block":"none");
}

function toggle_dein_info(is_exist){
	rd_info_dein_have.checked = is_exist;
	rd_info_dein_nothave.checked = !is_exist;
	table_deinsectzation_record.style.display = (is_exist?"block":"none");
}

function update_vac_info(vac_info) {
	rev_vac_id.value = vac_info.id;
	txt_info_vacdate.value = vac_info.date;
	txt_info_vacproduct.value = vac_info.product;
	txt_info_vacsite.value = vac_info.injection_site;
	txt_info_vacdoctor.value = vac_info.doctor;
	// TODO: next time to visit
}

function update_dei_info(dei_info) {
	txt_info_desdate.value = dei_info.date;
	txt_info_desproduct.value = dei_info.product;
	txt_info_desdoctor.value = dei_info.doctor;
	// TODO: next time to visit
}

function update_hs_info(hs_info) {
	txt_info_hospitaldate.value = hs_info.start_date;
	txt_info_hospitalbackdate.value = hs_info.end_date;
}

function update_opc_info(opc_info){
	txt_info_outpatientdate.value = opc_info.date;
	//txt_info_outpatient_nexttime.value = opc_info.
	txt_info_symptom.value = opc_info.symptom;
}

function update_hem_info(hem_info){
	txt_info_RBC.value = hem_info.RBC;
	txt_info_HCT.value = hem_info.HCT;
	txt_info_PHOS.value = hem_info.CGB;
	txt_info_MCH.value = hem_info.MCH;
	txt_info_MCHC.value = hem_info.MCHC;
}

function update_kid_info(kid_info){
	txt_info_CREA.value = kid_info.CREA;
	txt_info_BUM.value = kid_info.BUM;
	txt_info_PHOS.value = kid_info.PHOS;
	txt_info_CA.value = kid_info.CA;
	txt_info_ALB.value = kid_info.ALB;
	txt_info_CHOL.value = kid_info.CHOL;
    txt_info_PCT.value = kid_info.PCT;
}

function update_website_pet_info(pet_info) {
	var info = pet_info.info, vac_info = pet_info.vaccination,
		dei_info = pet_info.deinsectzation;
	txt_info_animalname.value = info.name;
	txt_info_birthday.value = info.birthday;
	txt_info_color.value = info.color;
	rd_info_gender_male.checked = (info.gender === 'M');
	rd_info_gender_female.checked = !rd_info_gender_male.checked;
	txt_info_weight.value = info.weight;
	rd_info_neuter_yes.checked = (info.neuter === 'Y');
	rd_info_neuter_no.checked = !rd_info_neuter_yes.checked;
	txt_info_varity.value = info.breed;
	rev_pet_id.value = info.id;
	if (vac_info.length > 0) {
		console.log('update');
		toggle_vac_info(true);
		select_vac_history.innerHTML = '';
		var _tmp = '';
		for (var i = 0; i < vac_info.length; i++) {
			_tmp += '<option value="'+ i +'">'+ vac_info[i].date +'</option>';
		}
		select_vac_history.innerHTML = _tmp;
		update_vac_info(vac_info[0]);
	} else {	
		toggle_vac_info(false);
	}
	if (dei_info.length > 0) {
		toggle_dein_info(true);
		select_dei_history.innerHTML = '';
		var _tmp = '';
		for (var i = 0; i < dei_info.length; i++) {
			_tmp += '<option value="' + i + '">'+ dei_info[i].date +'</option>';
		}
		update_dei_info(dei_info[0]);
		select_dei_history.innerHTML = _tmp;
	} else {
		toggle_dein_info(false);
	}
}


function get_pet_info() {
	$.getJSON('/request.php?t=pet_detail&user_id=' + user_id + '&' + new Date().getTime(), function(json_data) {
		console.log(json_data);
		pet_data_store = json_data.data;
		if (json_data.data.length > 0) {
			var tmp_dropdown_select = '';
			button_update_pet_information.disabled = false;
			dropdown_pet_select.disabled = false;
			dropdown_pet_select.innerHTML = ''; // reset options
			for (var i =0 ; i < pet_data_store.length; i++) {
				tmp_dropdown_select += '<option value="' + i +'">'+ pet_data_store[i].info.name +'</option>';
			}
			// use 1 information to default information
			update_website_pet_info(pet_data_store[0]);
			dropdown_pet_select.innerHTML = tmp_dropdown_select;
		}
	});
}

function do_POST(t, payload, callback = (result) => {console.log(result);}) {
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