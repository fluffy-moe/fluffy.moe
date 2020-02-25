'use strict';
var txt_info_name, txt_info_email, txt_info_phone, txt_info_address,
	txt_info_animalname, rd_info_gender_male, rd_info_gender_female,
	txt_info_varity, txt_info_color, txt_info_picture, txt_info_birthday,
	txt_info_age, txt_info_weight, rd_info_neuter_yes, rd_info_neuter_no,
	rd_vac_have, rd_vac_nothave, txt_info_vacdate, txt_info_vacproduct,
	txt_info_vacsite, txt_info_vacdoctor, txt_info_vacnextdate, rd_dein_have,
	rd_dein_nothave, txt_info_desdate, txt_info_desproduct, txt_info_desdoctor,
	txt_info_desnextdate, txt_info_hospitaldate, txt_info_hospitalbackdate,
	txt_info_outpatientdate, txt_info_outpatient_nexttime, txt_info_symptom,
	txt_info_RBC, txt_info_HCT, txt_info_MCH, txt_info_MCHC, txt_info_CREA,
	txt_info_BUM, txt_info_PHOS, txt_info_CA, txt_info_ALB, txt_info_CHOL,
	txt_info_CGB,
	txt_info_PCT, dropdown_pet_select, select_vac_history, select_dei_history,
	select_hs_history, select_opc_history, select_hem_history, select_kid_history;

var button_update_personal_information, button_update_pet_information, button_add_new_pet;

var table_vaccination_record, table_deinsectzation_record, table_Hospital_admission_record, table_Outpatient_Clinic_record,
    table_Hematology_record, table_Kidney_record;

var rev_user_id, rev_pet_id, rev_vac_id, rev_dein_id, rev_hs_id, rev_opc_id, rev_hem_id, rev_kid_id;


var rd_hs_have, rd_hs_nothave, rd_opc_have, rd_opc_nothave, rd_hem_have, rd_hem_nothave, rd_kid_have, rd_kid_nothave;
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
	rd_vac_have = document.getElementById('rb_vac_have');
	rd_vac_nothave = document.getElementById('rb_vac_nothave');
	rd_dein_have = document.getElementById('rb_dein_have');
	rd_dein_nothave = document.getElementById('rb_dein_nothave');
	txt_info_vacdate = document.getElementById('info_vacdate');
	txt_info_vacproduct = document.getElementById('info_vacproduct');
	txt_info_vacsite = document.getElementById('info_vacsite');
	txt_info_vacdoctor = document.getElementById('info_vacdoctor');
	txt_info_vacnextdate = document.getElementById('info_vacnextdate');
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
	table_Hospital_admission_record = document.getElementById('table_Hospital_admission_record');
	table_Outpatient_Clinic_record = document.getElementById('table_Outpatient_Clinic_record');
	table_Hematology_record = document.getElementById('table_Hematology_record');
	table_Kidney_record = document.getElementById('table_Kidney_record');

	rev_user_id = document.getElementById('rev_user_id');
	rev_pet_id = document.getElementById('rev_pet_id');
	rev_vac_id = document.getElementById('rev_vac_id');
	rev_dein_id = document.getElementById('rev_dein_id');
	rev_hs_id = document.getElementById('rev_hs_id');
	rev_opc_id = document.getElementById('rev_opc_id');
	rev_hem_id = document.getElementById('rev_hem_id');
	rev_kid_id = document.getElementById('rev_kid_id');

	select_vac_history = document.getElementById('select_vac_history');
	select_dei_history = document.getElementById('select_dei_history');
	select_hs_history = document.getElementById('select_hs_history');
	select_opc_history = document.getElementById('select_opc_history');
	select_hem_history = document.getElementById('select_hem_history');
	select_kid_history = document.getElementById('select_kid_history');

	rd_hs_have = document.getElementById('rb_hs_have');
	rd_hs_nothave = document.getElementById('rb_hs_nothave');
	rd_opc_have = document.getElementById('rb_opc_have');
	rd_opc_nothave = document.getElementById('rb_opc_nothave');
	rd_hem_have = document.getElementById('rb_hem_have');
	rd_hem_nothave = document.getElementById('rb_hem_nothave');
	rd_kid_have = document.getElementById('rb_kid_have');
	rd_kid_nothave = document.getElementById('rb_kid_nothave');
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
				//console.log('test');
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

	rd_vac_have.addEventListener('click', () => {
		table_vaccination_record.style.display = "block";
	});

	rd_vac_nothave.addEventListener('click', () => {
		table_vaccination_record.style.display = "none";
	});

	rd_dein_have.addEventListener('click', () => {
		table_deinsectzation_record.style.display = "block";
	});

	rd_dein_nothave.addEventListener('click', () => {
		table_deinsectzation_record.style.display = "none";
	});

	rd_hs_have.addEventListener('click', () => {
		table_Hospital_admission_record.style.display = "block";
	});

	rd_hs_nothave.addEventListener('click', () => {
		table_Hospital_admission_record.style.display = "none";
	});

	rd_opc_have.addEventListener('click', () => {
		table_Outpatient_Clinic_record.style.display = "block";
	});

	rd_opc_nothave.addEventListener('click', () => {
		table_Outpatient_Clinic_record.style.display = "none";
	});

	rd_hem_have.addEventListener('click', () => {
		table_Hematology_record.style.display = "block";
	});

	rd_hem_nothave.addEventListener('click', () => {
		table_Hematology_record.style.display = "none";
	});

    rd_kid_have.addEventListener('click', () => {
		table_Kidney_record.style.display = "block";
	});

	rd_kid_nothave.addEventListener('click', () => {
		table_Kidney_record.style.display = "none";
	});

	select_vac_history.addEventListener('change', () => {
		update_vac_info(pet_data_store[dropdown_pet_select.value].vaccination[select_vac_history.value]);
	});

	select_dei_history.addEventListener('change', () => {
		update_dei_info(pet_data_store[dropdown_pet_select.value].deinsectzation[select_dei_history.value]);
	});

	select_hs_history.addEventListener('change', () => {
		update_hs_info(pet_data_store[dropdown_pet_select.value].hospital_admission[select_hs_history.value]);
	});

	select_opc_history.addEventListener('change', () => {
		update_opc_info(pet_data_store[dropdown_pet_select.value].outpatient_clinic[select_opc_history.value]);
	});

	select_hem_history.addEventListener('change', () => {
		update_hem_info(pet_data_store[dropdown_pet_select.value].hematology_test[select_hem_history.value]);
	});

	select_kid_history.addEventListener('change', () => {
		update_kid_info(pet_data_store[dropdown_pet_select.value].kidney_test[select_kid_history.value]);
	});

	$('#btn_add_vac_info').click(() => {
		if (select_vac_history.innerHTML.search('<option>new</option>') === -1) {
			select_vac_history.innerHTML += '<option>new</option>';
		}
		d = new Date();
		txt_info_vacdate.value = d.getYear() + '/' + d.getMonth() + '/' + d.getDay();
		txt_info_vacproduct.value = '';
		txt_info_vacsite.value = '';
		txt_info_vacdoctor.value = '';
		rev_vac_id.value = '';
	});

	$('#btn_add_dei_info').click(() => {
		if (select_dei_history.innerHTML.search('<option>new</option>') === -1) {
			select_dei_history.innerHTML += '<option>new</option>';
		}
		d = new Date();
		txt_info_deidate.value = d.getYear() + '/' + d.getMonth() + '/' + d.getDay();
		txt_info_deiproduct.value = '';
		txt_info_deidoctor.value = '';
		rev_dei_id.value = '';
	});

	$('#btn_add_hos_info').click(() => {
		if (select_hs_history.innerHTML.search('<option>new</option>') === -1) {
			select_hs_history.innerHTML += '<option>new</option>';
		}
		d = new Date();
		txt_info_hospitaldate.value = d.getYear() + '/' + d.getMonth() + '/' + d.getDay();
		txt_info_hospitalbackdate.value = d.getYear() + '/' + d.getMonth() + '/' + d.getDay();
		rev_hs_id.value = '';
	});

	$('#btn_add_opc_info').click(() => {
		if (select_opc_history.innerHTML.search('<option>new</option>') === -1) {
			select_opc_history.innerHTML += '<option>new</option>';
		}
		d = new Date();
		txt_info_outpatientdate.value = d.getYear() + '/' + d.getMonth() + '/' + d.getDay();
		txt_info_symptom.value = '';
		rev_opc_id.value = '';
	});

	$('#btn_add_hem_info').click(() => {
		if (select_hem_history.innerHTML.search('<option>new</option>') === -1) {
			select_hem_history.innerHTML += '<option>new</option>';
		}
		d = new Date();
		txt_info_hematologydate.value = d.getYear() + '/' + d.getMonth() + '/' + d.getDay();
		txt_info_RBC.value = '';
		txt_info_HCT.value = '';
		txt_info_CGB.value = '';
		txt_info_MCH.value = '';
		txt_info_MCHC.value = '';
		rev_hem_id.value = '';
	});

	$('#btn_add_kid_info').click(() => {
		if (select_kid_history.innerHTML.search('<option>new</option>') === -1) {
			select_kid_history.innerHTML += '<option>new</option>';
		}
		d = new Date();
		txt_info_kidneydate.value = d.getYear() + '/' + d.getMonth() + '/' + d.getDay();
		txt_info_CREA.value = '';
		txt_info_BUM.value = '';
		txt_info_PHOS.value = '';
		txt_info_CA.value = '';
		txt_info_ALB.value = '';
		txt_info_CHOL.value = '';
		txt_info_PCT.value = '';
		rev_kid_id.value = '';
	});

	$('#btn_update_vac_info').click(() => {
		do_POST('update_vac', {
			id: rev_vac_id.value,
			date: txt_info_vacdate.value,
			product: txt_info_vacproduct.value,
			injection_site: txt_info_vacsite.value,
			doctor: txt_info_vacdoctor.value
		});
	});

	$('#btn_update_dein_info').click(() => {
		do_POST('update_dein', {
			id: rev_dein_id.value,
			date: txt_info_desdate.value,
			product: txt_info_desproduct.value,
			doctor: txt_info_desdoctor.value
		});
	});

	$('#btn_update_hs_info').click(() => {
        do_POST('update_hs',{
			id: rev_hs_id.value,
			start_date: txt_info_hospitaldate.value,
			end_date: txt_info_hospitalbackdate.value
		});
	});

	$('#btn_update_opc_info').click(() => {
        do_POST('update_opc',{
			id: rev_opc_id.value,
			date: txt_info_outpatientdate.value,
			symptom: txt_info_symptom.value
		});
	});

    $('#btn_update_hem_info').click(() => {
        do_POST('update_hem',{
			id: rev_hem_id.value,
			date: txt_info_hematologydate.value,
			RBC: txt_info_RBC.value,
			HCT: txt_info_HCT.value,
			CGB: txt_info_CGB.value,
			MCH: txt_info_MCH.value,
			MCHC: txt_info_MCHC.value
		});
	});

	$('#btn_update_kt_info').click(() => {
        do_POST('update_kid',{
			id: rev_kid_id.value,
			date: $('#info_kiddate').val(),
			CREA: txt_info_CREA.value,
			BUM: txt_info_BUM.value,
			PHOS: txt_info_PHOS.value,
			CA: txt_info_CA.value,
			ALB: txt_info_ALB.value,
			CHOL: txt_info_CHOL.value,
			PCT: txt_info_PCT.value,
		});
	});

	$('#btn_del_vac_info').click(() => {
		var c = confirm("Are you sure you want to delete?");
		if (c === true) {
			do_POST('del_vac_info', {
				id: rev_vac_id.value
			});
			rev_vac_id.value = '';
			txt_info_vacdate.value = '';
			txt_info_vacproduct.value = '';
			txt_info_vacsite.value = '';
			txt_info_vacdoctor.value = '';
		}
	});

	$('#btn_del_dei_info').click(() => {
		var c = confirm("Are you sure you want to delete?");
		if (c === true) {
			do_POST('del_dei_info', {
				id: rev_dei_id.value
			});
			rev_dei_id.value = '';
			txt_info_deidate.value = '';
			txt_info_deiproduct.value = '';
			txt_info_deidoctor.value = '';
		}
	});

	$('#btn_del_hos_info').click(() => {
		var c = confirm("Are you sure you want to delete?");
		if (c === true) {
			do_POST('del_hos_info', {
				id: rev_hs_id.value
			});
			rev_hs_id.value = '';
			txt_info_hospitaldate.value = '';
			txt_info_hospitalbackdate.value = '';
		}
	});

	$('#btn_del_opc_info').click(() => {
		var c = confirm("Are you sure you want to delete?");
		if (c === true) {
			do_POST('del_opc_info', {
				id: rev_opc_id.value
			});
			rev_opc_id.value = '';
			txt_info_outpatientdate.value = '';
			txt_info_symptom.value = '';
		}
	});

	$('#btn_del_hem_info').click(() => {
		var c = confirm("Are you sure you want to delete?");
		if (c === true) {
			do_POST('del_hem_info', {
				id: rev_hem_id.value
			});
			rev_hem_id.value = '';
			txt_info_hematologydate.value = '';
			txt_info_RBC.value = '';
			txt_info_HCT.value = '';
			txt_info_CGB.value = '';
			txt_info_MCH.value = '';
			txt_info_MCHC.value = '';
		}
	});

	$('#btn_del_kid_info').click(() => {
		var c = confirm("Are you sure you want to delete?");
		if (c === true) {
			do_POST('del_kid_info', {
				id: rev_kid_id.value
			});
			rev_kid_id.value = '';
			txt_info_kidneydate.value = '';
			txt_info_CREA.value = '';
			txt_info_BUM.value = '';
			txt_info_PHOS.value = '';
			txt_info_CA.value = '';
			txt_info_ALB.value = '';
			txt_info_CHOL.value = '';
			txt_info_PCT.value = '';
		}
	});


	get_user_info();
}

/**
 * NOTE: must check return type if you want to use return type
 */
function get_user_info() {
	user_id = findGetParameter('user');
	rev_user_id.value = user_id;
	//console.log(user_id);
	var rt;
	if (user_id !== null) {
		rt = $.getJSON('/request.php?t=user_detail&user_id=' + user_id + '&' + new Date().getTime(), function(json_data) {
			//console.log(json_data);
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
	rd_vac_have.checked = is_exist;
	rd_vac_nothave.checked = !is_exist;
	table_vaccination_record.style.display = (is_exist?"block":"none");
}

function toggle_dein_info(is_exist){
	rd_dein_have.checked = is_exist;
	rd_dein_nothave.checked = !is_exist;
	table_deinsectzation_record.style.display = (is_exist?"block":"none");
}

function toggle_hs_info(is_exist){
	rd_hs_have.checked = is_exist;
	rd_hs_nothave.checked = !is_exist;
	table_Hospital_admission_record.style.display = (is_exist?"block":"none");
}

function toggle_opc_info(is_exist){
	rd_opc_have.checked = is_exist;
	rd_opc_nothave.checked = !is_exist;
	table_Outpatient_Clinic_record.style.display = (is_exist?"block":"none");
}

function toggle_hem_info(is_exist){
	rd_hem_have.checked = is_exist;
	rd_hem_nothave.checked = !is_exist;
	table_Hematology_record.style.display = (is_exist?"block":"none");
}

function toggle_kid_info(is_exist){
	rd_kid_have.checked = is_exist;
	rd_kid_nothave.checked = !is_exist;
	table_Kidney_record.style.display = (is_exist?"block":"none");
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
	rev_hs_id.value = hs_info.id;
	txt_info_hospitaldate.value = hs_info.start_date;
	txt_info_hospitalbackdate.value = hs_info.end_date;
}

function update_opc_info(opc_info){
	rev_opc_id.value = opc_info.id;
	txt_info_outpatientdate.value = opc_info.date;
	//txt_info_outpatient_nexttime.value = opc_info.
	txt_info_symptom.value = opc_info.symptom;
}

function update_hem_info(hem_info){
	rev_hem_id.value = hem_info.id;
	$('#info_hemdate').val(hem_info.date);
	txt_info_RBC.value = hem_info.RBC;
	txt_info_HCT.value = hem_info.HCT;
	txt_info_PHOS.value = hem_info.PHOS;
	txt_info_CGB.value = hem_info.CGB;
	txt_info_MCH.value = hem_info.MCH;
	txt_info_MCHC.value = hem_info.MCHC;
}

function update_kid_info(kid_info){
	rev_kid_id.value = kid_info.id;
	$('#info_kiddate').val(kid_info.date);
	txt_info_CREA.value = kid_info.CREA;
	txt_info_BUM.value = kid_info.BUM;
	txt_info_PHOS.value = kid_info.PHOS;
	txt_info_CA.value = kid_info.CA;
	txt_info_ALB.value = kid_info.ALB;
	txt_info_CHOL.value = kid_info.CHOL;
    txt_info_PCT.value = kid_info.PCT;
}

function clear_additional_info() {
	rev_vac_id.value = '';
	txt_info_vacdate.value = '';
	txt_info_vacproduct.value = '';
	txt_info_vacsite.value = '';
	txt_info_vacdoctor.value = '';
	// TODO: next time to visit
	txt_info_desdate.value = '';
	txt_info_desproduct.value = '';
	txt_info_desdoctor.value = '';
	// TODO: next time to visit
	rev_hs_id.value = '';
	txt_info_hospitaldate.value = '';
	txt_info_hospitalbackdate.value = '';
	rev_opc_id.value = '';
	txt_info_outpatientdate.value = '';
	//txt_info_outpatient_nexttime.value = ''
	txt_info_symptom.value = '';
	rev_hem_id.value = '';
	txt_info_RBC.value = '';
	txt_info_HCT.value = '';
	txt_info_PHOS.value = '';
	txt_info_MCH.value = '';
	txt_info_MCHC.value = '';
	rev_kid_id.value = '';
	txt_info_CREA.value = '';
	txt_info_BUM.value = '';
	txt_info_PHOS.value = '';
	txt_info_CA.value = '';
	txt_info_ALB.value = '';
	txt_info_CHOL.value = '';
    txt_info_PCT.value = '';
}

function update_website_pet_info(pet_info) {
	var info = pet_info.info, vac_info = pet_info.vaccination,
		dei_info = pet_info.deinsectzation, hs_info = pet_info.hospital_admission,
		opc_info = pet_info.outpatient_clinic, hem_info = pet_info.hematology_test,
		kid_info = pet_info.kidney_test;
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

	if (hs_info.length > 0) {
		toggle_hs_info(true);
		select_hs_history.innerHTML = '';
		var _tmp = '';
		for (var i = 0; i < hs_info.length; i++) {
			_tmp += '<option value="' + i + '">'+ hs_info[i].start_date +'</option>';
		}
		update_hs_info(hs_info[0]);
		select_hs_history.innerHTML = _tmp;
	} else {
		toggle_hs_info(false);
	}

	if (opc_info.length > 0) {
		toggle_opc_info(true);
		select_opc_history.innerHTML = '';
		var _tmp = '';
		for (var i = 0; i < opc_info.length; i++) {
			_tmp += '<option value="' + i + '">'+ opc_info[i].date +'</option>';
		}
		update_opc_info(opc_info[0]);
		select_opc_history.innerHTML = _tmp;
	} else {
		toggle_opc_info(false);
	}

	if (hem_info.length > 0) {
		toggle_hem_info(true);
		select_hem_history.innerHTML = '';
		var _tmp = '';
		for (var i = 0; i < hem_info.length; i++) {
			_tmp += '<option value="' + i + '">'+ hem_info[i].date +'</option>';
		}
		update_hem_info(hem_info[0]);
		select_hem_history.innerHTML = _tmp;
	} else {
		toggle_hem_info(false);
	}

	if (kid_info.length > 0) {
		toggle_kid_info(true);
		select_kid_history.innerHTML = '';
		var _tmp = '';
		for (var i = 0; i < kid_info.length; i++) {
			_tmp += '<option value="' + i + '">'+ kid_info[i].date +'</option>';
		}
		update_kid_info(kid_info[0]);
		select_kid_history.innerHTML = _tmp;
	} else {
		toggle_kid_info(false);
	}

}


function get_pet_info() {
	$.getJSON('/request.php?t=pet_detail&user_id=' + user_id + '&' + new Date().getTime(), function(json_data) {
		//console.log(json_data);
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