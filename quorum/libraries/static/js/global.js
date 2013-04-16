$(function() {
	slideInSublibraries();

	autoComplete();

	registerWithGoogle();

	registrationValidateAndSubmit();

	userSignIn();

	expandAndCollapseLeftSideMenu();

	createRatingControls();

	fiveStarRatings();

    submitCodeSample();

    fadeInLibraryTable();

	extendLeftSidebar(); // keep this at the end
});

function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
        vars[key] = value;
    });
    return vars;
}

function refresh() {
	location.reload();
}

var bodyMessage = function(text, state) {
	var element = '<div class="alert alert-' + state + ' alert-body-bottom-right" style="display:none">' + text + '</div>'
	$("body").append(element);
	
	$(".alert-body-bottom-right").fadeIn('slow', function(){
		$(this).delay(5000).fadeOut();
	});
}

var successMessage = function(text) {
	bodyMessage(text, "success");
}

var errorMessage = function(text) {
	bodyMessage(text, "error");
}

var slideInSublibraries = function() {
	var toggleList = function(e, gridItem) {
		var isTargetSublistItem = ($(e.target).hasClass("sublist-item")) ? true : ($(e.target).parent().hasClass("sublist-item"));
		if (isTargetSublistItem) { return; }

		var sublist = gridItem.find(".grid-sublist");
		if (sublist.length == 0) { delete sublist; return; }
		e.preventDefault();

		$(sublist).slideToggle();
	}

	$(".grid-item").on("click", function(e) { toggleList(e,$(this)); });

}

var extendLeftSidebar = function() {
	var contentHeight = $(".content-wrapper").height();
	$(".navigation-sidebar").css("min-height", contentHeight + "px");
}

var autoComplete = function() {
	var searchTerms = ["Math", "Random", "Array", "List", "Queue", "Stack", "Table", "Addable", "ArrayBlueprint", "Container", "Copyable", "Indexed", "Iterative", "Iterator", "KeyedAddable", "KeyedIterative", "ListBlueprint", "QueueBlueprint", "StackBlueprint", "TableBlueprint", "ArrayIterator", "KeyedNode", "ListIterator", "ListNode", "Object", "CastError", "DivideByZeroError", "EndOfFileError", "Error", "FileNotFoundError", "InputOutputError", "InvalidArgumentError", "InvalidLocationError", "InvalidPathError", "OutOfBoundsError", "UndefinedObjectError", "CompareResult", "Boolean", "Integer", "Number", "Text", "AnalogSensor", "Button", "Connectable", "Controller", "DigitalSensor", "Motor", "Robot", "Servo", "Audio", "Chord", "Instrument", "Music", "MusicEvent", "Note", "Playable", "Speech", "Track", "Console", "DateTime", "File", "FileRandomAccess", "FileReader", "FileWriter", "Path", "StackTraceItem", "FileRandomAccessBlueprint", "FileReaderBlueprint", "FileWriterBlueprint"];

	$(".search-query").typeahead({
      source: searchTerms,
      minLength: 3
    });	
}

var registerWithGoogle = function() {
	if (getUrlVars()["loginWith"] == "google") {
		$('#modal-registration').modal();
	}
}

var validateEmail = function(email) {
	var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	
	if ((re.test(email) == false) || (email.length <= 0)) {
		return false;
	}

	return true;
}

var validateUsername = function(username) {
	var re = /[-!$%^&*()_+|~=`{}\[\]:";'<>?,.\/]/;

	if (re.test(username) || (username.indexOf(' ') >= 0) || (username.length <= 0)) {
		return false;
	}

	return true;
}

var validatePassword = function(password) {
	if (password !== undefined && password.length <= 0) {
		return false;
	}

	return true;
}

var registerUser = function() {
	var buttons = $("#modal-registration .modal-footer button");
	var spinner = $("#modal-registration .modal-footer .loading-spinner");

	buttons.hide();
	spinner.show();

	$.ajax({
		type: "POST",
		url: "/controllers/user.controller.php?action=register",
		data: $("#registration-form").serialize(),
		success: function(result) {
			if ($.trim(result) == "1") {
				successMessage("<strong>You have successfully registered.</strong> Welcome to the Quorum website!");
				showUserHeaderControls();
				$('#modal-registration').modal('hide');
				spinner.hide();
			}
			else {
				$("#registration-form #integrity-error").remove();
				$("#registration-form").before('<duv class="text-error" id="integrity-error">Sorry, but the email or username entered has been registered already.</div>');
				$("#integrity-error").show();
				buttons.show();
				spinner.hide();
			}
		},
		error: function (xhr, ajaxOptions, thrownError) {
			errorMessage("<strong>Sorry!</strong> There was a server error and your registration could not be completed.");
			$('#modal-registration').modal('hide');
			spinner.hide();
		}
	});
}

var showUserHeaderControls = function() {
	$.ajax({
		url: "/static/templates/user-headercontrols.template.php",
		context: document.body
	}).done(function(data) {
		$(".user-controls-loggedout").remove();
		$(".user-controls").html(data);
	});
}


var loginDisplayValidateAndSubmit = function() {
	$('#modal-login').modal();
	$("#modal-registration button.btn-primary").on("click", function() {
		$('#modal-login').modal("show");
	});
}

var registrationValidateAndSubmit = function() {
	$("#modal-registration button.btn-primary").on("click", function() {
		var emailField = $("#registration-email");
		var usernameField = $("#registration-username");
		var passwordField = $("#registration-password");

		var email = emailField.val();
		var username = usernameField.val();
		var password = passwordField.val();

		var textError = $("#modal-registration .text-error");
		var emailError = $(textError[0]);
		var usernameError = $(textError[1]);
		var passwordError = $(textError[2]);

		var emailIsValid = validateEmail(email);
		var usernameIsValid = validateUsername(username);
		var passwordIsValid = validatePassword(password);

		emailError.hide();
		usernameError.hide();
		passwordError.hide();

		if (emailIsValid && usernameIsValid && passwordIsValid) {
			registerUser();
		}
		else {
			if (!emailIsValid) {
				emailError.show();
			}
			if (!usernameIsValid) {
				usernameError.show();
			}
			if (!passwordIsValid) {
				passwordError.show();
			}

			return false;
		}
	});

	$("#registration-email").on("blur", function() {
		var emailError = $("#modal-registration .email .text-error");

		if ($(this).val() == "" || validateEmail($(this).val())) {
			emailError.hide();
		}
		else {
			emailError.show();
		}
	});

	$("#registration-username").on("blur", function() {
		var usernameError = $("#modal-registration .username .text-error");

		if ($(this).val() == "" || validateUsername($(this).val())) {
			usernameError.hide();
		}
		else {
			usernameError.show();
		}
	});

	$("#registration-password").on("blur", function() {
		var passwordError = $("#modal-registration .password .text-error");

		if ($(this).val() == "" || validatePassword($(this).val())) {
			passwordError.hide();
		}
		else {
			passwordError.show();
		}
	});
}

var userSignIn = function() {
	var checkCredentials = function(username, password) {
		var buttons = $("#modal-login .modal-footer button");
		var spinner = $("#modal-login .modal-footer .loading-spinner");

		$.ajax({
			type: "POST",
			url: "/controllers/user.controller.php?action=login",
			data: $("#login-form").serialize(),
			success: function(result) {
				console.log(result);
				if ($.trim(result) == "1") {
					refresh();
				}
				else {
					$("#modal-login #integrity-error").remove();
					$("#login-form").before('<div class="text-error" id="integrity-error">Sorry, but that login is not correct.</div>');
					$("#integrity-error").show();
					buttons.show();
					spinner.hide();
				}
			},
			error: function (xhr, ajaxOptions, thrownError) {
				errorMessage("<strong>Sorry!</strong> There was a server error and your login could not be completed.");
				$('#modal-registration').modal('hide');
				spinner.hide();
			}
		});
	}

	$("#modal-login .btn-primary").on("click", function() {
		checkCredentials($("#login-username"), $("#login-password"));
	});
}

var expandAndCollapseLeftSideMenu = function() {
	$(".child").hide();

	$(".collapsable").on("click", function() {
		$(this).parent().children().toggle();
		$(this).toggle();
	});

	$.each($(".collapsable"), function() {
		if ($(this).parent().find('.child').length > 0) {
			$(this).append(" [+]");
		}
	});

}

var createRatingControls = function() {
	var template = doT.template($('#template-ratings-controls').text());

	$.each($(".controllable"), function() {
		var componentType = $(this).data("componenttype");
		componentType = (componentType !== undefined) ? componentType.replace(/-/g," ") : "component";
		var templateData = { id: $(this).data("id"), componentType: componentType }; // TODO: get real value
		$(this).append(template(templateData));
	});

	$('.star-ratings a').tooltip({'selector': '', 'placement': 'bottom'});

	var getRatingForId = function(id) {
		// TODO: make an ajax call to the database to get the real rating
		
		return 3;
	}
}

var fiveStarRatings = function(starRatingsList) {
	var numberOfStars = function (clickTarget) {
		return ($(clickTarget).attr('class').split(/-/))[1]; // gets the class, splits at the hyphen, and grabs the number
	}

	var setStars = function(element, starNumber) {
		$(element)
			.removeClass("stars-0").removeClass("stars-1").removeClass("stars-2").removeClass("stars-3").removeClass("stars-4").removeClass("stars-5")
			.addClass("stars-" + starNumber);
	}

	var postRating = function(controllable, starNumber) {
		var username = $("input#username").val();
		var classStaticKey = $("input#classkey").val();
		var componentType = controllable.data("componenttype")
		var postData = { username: username, classstatickey: classStaticKey, rating: starNumber, componenttype: componentType };
		switch (componentType) {
			case "class-name": case "class-description": case "class-example": break;
			case "action-name": case "action-description": case "action-example": 
				postData['actionkey'] = controllable.data("actionkey");
				break;
			case "parameter-name": case "parameter-description":
				postData['actionkey'] = controllable.closest(".action").find(".controllable[data-componenttype=action-name]").data("actionkey");
				postData['parameterkey'] = controllable.data("parameterkey");
				break;
		}

		$.ajax({
			type: "POST",
			url: "/controllers/ratings.controller.php?action=submitRating", // TODO: fix this URL
			dataType: "json",
			data: postData,
			success: function(result) {
				// show a success message
				console.info(result);
			},
			error: function (xhr, ajaxOptions, thrownError) {
				// show an error message
				console.error(xhr.status);
				console.error(thrownError);
			}
		});
	}

	$(".star-ratings li").on({
		mouseenter: function() {
			setStars($(this).parent(), numberOfStars(this));
		},
		mouseleave: function() { 
			setStars($(this).parent(), 0);
		},
		click: function(e) {
			e.preventDefault();
			var starNumber = numberOfStars(this);
			setStars($(this).parent(), starNumber);

			$(this).parent().find('li').unbind('mouseenter').unbind('mouseleave');

			var template = doT.template($('#template-ratings-success').text());
			var templateHtml = template();
			var container = $(this).closest(".ratings-controls");
			container.find(".ratings-success").remove();
			container.append(templateHtml);

			postRating($(this).closest(".controllable"), starNumber);
		}
	});
}

var submitCodeSample = function(){
	$("#run").on("click", function(e) {
		e.preventDefault();
		$(".outputArea").text("");
		
		$.ajax({
			type:"POST",
			url: "controllers/IDE.controller.php",
			data:{code: $(".inputArea").val()},
			dataType: "json",
			success: function(returnData){
				$(".outputArea").text(returnData);
				console.log("suc" + returnData);
			},
			completed: function(returnData){
				$(".outputArea").text(returnData);
				console.log("comp" + returnData);
			}
		})
	})
}


var fadeInLibraryTable = function() {
	if ($(".index-grid").length > 0) {
		$(".index-package").hide();
		$(".index_package_title a").on("click", function() {
			$(".index-package").hide();	
			var anchorHash = $(this).attr("href");
			var tableId = $.trim("#table-" + (anchorHash.substring(1, anchorHash.length)));
			console.log(tableId);
			$(tableId).show();
		});
	}
}











































 