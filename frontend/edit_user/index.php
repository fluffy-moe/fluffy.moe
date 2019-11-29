<?php
	require_once('../.config.inc.php');
	if (!$_SESSION['valid']){
		echo '<html><head><title>'.$SITE_NAME.'</title></head><body>You haven\'t login, please<a href="/login.php">Login</a> first.</body></html>';
		die();
	}
	if (time() - $_SESSION["timeout"] > $LOGIN_TIMEOUT) {
		unset($_SESSION['valid']);
		header("Location: /login.php", true, 301);
	}
	// Update time after refresh this page
	$_SESSION["timeout"] = time();
?>
<!DOCTYPE html>
<html lang="en">

<head>
	<!-- Basic -->
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">

	<!-- Mobile Metas -->
	<meta name="viewport" content="width=device-width, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">

	<!-- Site Metas -->
	<title><?php echo $SITE_NAME; ?></title>

	<!-- Site Icons -->
	<link rel="shortcut icon" href="images/favicon.ico" type="image/x-icon" />
	<link rel="apple-touch-icon" href="images/apple-touch-icon.png">

	<!-- Bootstrap CSS -->
	<link rel="stylesheet" href="css/bootstrap.min.css">
	<!-- Site CSS -->
	<link rel="stylesheet" href="style.css">
	<!-- Responsive CSS -->
	<link rel="stylesheet" href="css/responsive.css">
	<!-- Custom CSS -->
	<link rel="stylesheet" href="css/custom.css">
	<script src="js/modernizr.js"></script><!-- Modernizr -->


	<!--[if lt IE 9]>
	<script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
	<script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
	<![endif]-->

</head>
<body id="page-top" class="politics_version">

	<!-- LOADER -->
	<div id="preloader">
		<div id="main-ld">
			<div id="loader"></div>
		</div>
	</div><!-- end loader -->
	<!-- END LOADER -->

	<!-- Navigation -->
	<nav class="navbar navbar-expand-lg navbar-dark fixed-top" id="mainNav">
	<div class="container">
		<a class="navbar-brand js-scroll-trigger" href="#page-top">
			<img class="img-fluid" src="images/logo.png" alt="" />
		</a>
		<button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
		  Menu
		<i class="fa fa-bars"></i>
		</button>
		<div class="collapse navbar-collapse" id="navbarResponsive">
		<ul class="navbar-nav text-uppercase ml-auto">
			<li class="nav-item">
			<a class="nav-link js-scroll-trigger active" href="#home"><?php echo $SITE_NAME; ?></a>
			</li>
			<li class="nav-item">
			<a class="nav-link js-scroll-trigger" href="#about">Information</a>
			</li>
			<li class="nav-item">
			<a class="nav-link js-scroll-trigger" href="#services">Vaccination&amp;Deinsectzation</a>
			</li>
			<li class="nav-item">
			<a class="nav-link js-scroll-trigger" href="#portfolio">Hospital Record</a>
			</li>
		</ul>
		</div>
	</div>
	</nav>

	<section id="home" class="main-banner parallaxie" style="background: url('uploads/dogbanner-01.jpeg')">
		<div class="heading">
			<h1>Welcome to <?php echo $SITE_NAME; ?></h1>
		</div>
	</section>
	<input type="hidden" id="rev_user_id">
	<div id="about" class="section wb">
		<div class="container">
			<div class="row">
				<div class="col-md-6">
					<div class="message-box">
						<h2>Personal information</h2>
						<p>
							<table>
							<tr>
								<td>Name</td>
								<td><input type="text" id="info_name"></td>
							</tr>
							<tr>
								<td>Email:</td>
								<td><input type="text" id="info_email"></td>
							</tr>
							<tr>
								<td>Telephone:</td>
								<td><input type="text" id="info_phone"></td>
							</tr>
							<tr>
								<td>Address:</td>
								<td><input type="text" id="info_address"></td>
							</tr>
							</table>
						</p>
						<p>
							<button id="btn_personal_information_update">Update</button>
						</p>
						<!--<a href="#" class="sim-btn hvr-bounce-to-top"><span>Contact Us</span></a>-->
					</div><!-- end messagebox -->
				</div><!-- end col -->

				<div class="col-md-6">
					<div class="message-box">
						<h2>Pet's information</h2>
						<p><table>
							<tr>
								<td>Choose:</td>
								<td>
									<select id="pets_select" disabled>
										<option>None</option>
									</select>
									<button id="btn_add_new_pet">Add new</button>
								</td>
							</tr>
							<tr>
								<td>Name</td>
								<td><input type="text" id="info_animalname"></td>
							</tr>
							<tr>
								<td>Gender:</td>
								<td>
									<label><input type="radio" name="gender" id="info_gender_male">body</label>
									<label><input type="radio" name="gender" id="info_gender_female" checked>giri</label>
								</td>
							</tr>
							<tr>
								<td>Breed:</td>
								<td><input type="text" id="info_varity"></td>
							</tr>
							<tr>
								<td>Color:</td>
								<td><input type="text" id="info_color"></td>
							</tr>
							<tr>
								<td>Picture:</td>
								<td><input type="text" id="info_picture"></td>
							</tr>
							<tr>
								<td>Birthday:</td>
								<td><input type="text" id="info_birthday"></td>
							</tr>
							<tr>
								<td>Age:</td>
								<td><input type="text" id="info_age" disabled></td>
							</tr>
							<tr>
								<td>Weight:</td>
								<td><input type="text" id="info_weight"></td>
							</tr>
							<tr>
								<td>Neuter:</td>
								<td>
									<label><input type="radio" name="is_neuter" id="info_neuter_yes">Yes</label>
									<label><input type="radio" name="is_neuter" id="info_neuter_no" checked>No</label>
								</td>
							</tr>
						</table>
					</p>
						<p><button id="btn_pets_information_update">Update</button></p>
					</div>

					</div><!-- end media -->
				</div><!-- end col -->
			</div><!-- end row -->
		</div><!-- end container -->


	<div id="services" class="section lb">
		<div class="container">
			<div class="section-title text-center">
				<h3>Vaccination&amp;Deinsectzation</h3>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="message-box">
						<h2>Vaccination</h2>
						<p>
							<table>
								<tr>
									<td>Vaccination record:</td>
									<td><label><input type="radio" name="neuter" id="info_vac_have">Have</label>
									<label><input type="radio" name="neuter" id="info_vac_nothave" checked>Don't have</label></td>
								</tr>
							</table>	
							<table id="table_vaccination_record" style="display: none;">
								<tr>
									<td>Date:</td>
									<td><input type="text" id="info_vacdate"></td>
								</tr>
								<tr>
									<td>Product:</td>
									<td><input type="text" id="info_vacproduct"></td>
								</tr>
								<tr>
									<td>Injection site:</td>
									<td><input type="text" id="info_vacsite"></td>
								</tr>
								<tr>
									<td>Doctor:</td>
									<td><input type="text" id="info_vacdoctor"></td>
								</tr>
								<tr>
									<td>Next time to visit:</td>
									<td><input type="text" id="info_vacnextdate"></td>
								</tr>
							</table>
						</p>

						<!--<a href="#" class="sim-btn hvr-bounce-to-top"><span>Contact Us</span></a>-->
					</div><!-- end messagebox -->
				</div><!-- end col -->

				<div class="col-md-6">
					<div class="message-box">
						<h2>Deinsectzation</h2>
						<p>
							<table>
								<tr>
									<td>Deinsectzation record:</td>
									<td>
										<label><input type="radio" name="dein" id="info_dein_have">Have</label>
										<label><input type="radio" name="dein" id="info_dein_nothave" checked>Don't have</label>
									</td>
								</tr>
							</table>
							<table id="table_deinsectzation_record" style="display: none;">
								<tr>
									<td>Date:</td>
									<td><input type="text" id="info_desdate"></td>
								</tr>
								<tr>
									<td>Product:</td>
									<td><input type="text" id="info_desproduct"></td>
								</tr>
								<tr>
									<td>Doctor:</td>
									<td><input type="text" id="info_desdoctor"></td>
								</tr>
								<tr>
									<td>Next time to visit:</td>
									<td><input type="text" id="info_desnextdate"></td>
								</tr>
							</table>
						</p>


					</div>

					</div><!-- end media -->
				</div><!-- end col -->
			</div><!-- end row -->
		</div><!--end container -->
	<div id="portfolio" class="section lb">
		<div class="container">
			<div class="row">
				<div class="col-md-6">
					<div class="message-box">
						<h2>Hospital admission</h2>
						<p>
							<table>
							<tr>
								<td>In hospital date</td>
								<td><input type="text" id="info_hospitaldate"></td>
							</tr>
							<tr>
								<td>leave hospital date:</td>
								<td><input type="text" id="info_hospitalbackdate"></td>
							</tr>


							</table>
						</p>


					</div><!-- end messagebox -->
				</div><!-- end col -->

				<div class="col-md-6">
					<div class="message-box">
						<h2>Out-patient Clinic</h2>
						<p><table>
							<tr>
								<td>date</td>
								<td><input type="text" id="info_outpatientdate"></td>
							</tr>

							<tr>
								<td>Next time to visit:</td>
								<td><input type="text" id="info_outpatient_nexttime"></td>
							</tr>
							<tr>
								<td>Symptom:</td>
								<td><input type="text" id="info_symptom"></td>
							</tr>

						</table>
					</p>

					</div>

					</div><!-- end media -->
				</div><!-- end col -->
			</div><!-- end row -->
		</div><!-- end container -->


	<div id="blog" class="section lb"><hr>
		<div class="container">
			<div class="row">
				<div class="col-md-6">
					<div class="message-box">
						<h2>Hematology Test</h2>
						<p>
							<table>
							<tr>
								<td>RBC</td>
								<td><input type="text" id="info_RBC"></td>
							</tr>
							<tr>
								<td>HCT:</td>
								<td><input type="text" id="info_HCT"></td>
							</tr>
							<tr>
								<td>CGB:</td>
								<td><input type="text" id="info_CGB"></td>
							</tr>
							<tr>
								<td>MCH:</td>
								<td><input type="text" id="info_MCH"></td>
							</tr>
							<tr>
								<td>MCHC:</td>
								<td><input type="text" id="info_MCHC"></td>
							</tr>
							</table>
						</p>


					</div><!-- end messagebox -->
				</div><!-- end col -->

				<div class="col-md-6">
					<div class="message-box">
						<h2>Kidney Test</h2>
						<p><table>
							<tr>
								<td>CREA</td>
								<td><input type="text" id="info_CREA"></td>
							</tr>

							<tr>
								<td>BUM:</td>
								<td><input type="text" id="info_BUM"></td>
							</tr>
							<tr>
								<td>PHOS:</td>
								<td><input type="text" id="info_PHOS"></td>
							</tr>
							<tr>
								<td>CA:</td>
								<td><input type="text" id="info_CA"></td>
							</tr>
							<tr>
								<td>ALB:</td>
								<td><input type="text" id="info_ALB"></td>
							</tr>
							<tr>
								<td>CHOL:</td>
								<td><input type="text" id="info_CHOL"></td>
							</tr>
							<tr>
								<td>PCT:</td>
								<td><input type="text" id="info_PCT"></td>
							</tr>
						</table></p>

					</div>

					</div><!-- end media -->
				</div><!-- end col -->
			</div><!-- end row -->
		</div><!-- end container -->

	<!-- ALL JS FILES -->
	<script src="js/all.js"></script>
	<!-- Camera Slider -->
	<script src="js/jquery.mobile.customized.min.js"></script>
	<script src="js/jquery.easing.1.3.js"></script>
	<script src="js/parallaxie.js"></script>
	<script src="js/headline.js"></script>
	<!-- Contact form JavaScript -->
	<script src="js/jqBootstrapValidation.js"></script>
	<script src="js/contact_me.js"></script>
	<!-- ALL PLUGINS -->
	<script src="js/custom.js"></script>
	<script src="js/jquery.vide.js"></script>

	<script src="user_information.js?<?php echo time();?>"></script>
	<script>
		$(document).ready(init_view);
	</script>
</body>
</html>