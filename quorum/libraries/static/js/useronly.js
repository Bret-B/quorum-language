$(function() {
	createRatingControls();

	fiveStarRatings();
});

var createRatingControls = function() {
	var getRatingForId = function(id) {
		console.log(id);

		return;	
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
		
		
		return 3;
	}

	var template = doT.template($('#template-ratings-controls').text());

	$.each($(".controllable"), function() {
		var controllable = $(this);

		var componentType = controllable.data("componenttype");
		componentType = (componentType !== undefined) ? componentType : "component";

		var templateData = { componentType: componentType };
		switch (componentType) {
			case "class-name": case "class-description": case "class-example": 
				templateData['classkey'] = $("input#classkey").val();
				break;
			case "action-name": case "action-description": case "action-example": 
				templateData['classkey'] = $("input#classkey").val();
				templateData['actionkey'] = controllable.data("actionkey");
				break;
			case "parameter-name": case "parameter-description":
				templateData['classkey'] = $("input#classkey").val();
				templateData['actionkey'] = controllable.closest(".action").find(".controllable[data-componenttype=action-name]").data("actionkey");
				templateData['parameterkey'] = controllable.data("parameterkey");
				break;
		}

		templateData['choice'] = componentType.replace(/-/g, ' ');

		$(this).append(template(templateData));

		getRatingForId(templateData);
		console.log(componentType);
	});

	$('.star-ratings a').tooltip({'selector': '', 'placement': 'bottom'});
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
		var username = $("input[name=hidden-username]").val();
		var classStaticKey = $("input#classkey").val();
		var componentType = controllable.data("componenttype")
		var postData = { username: username, classstatickey: classStaticKey, rating: starNumber, componenttype: componentType };

		switch (componentType) {
			case "class-name": case "class-description": case "class-example": break;
			case "action-name": case "action-description": case "action-example": 
				postData['actionkey'] = controllable.data("actionkey");
				console.log("action");
				break;
			case "parameter-name": case "parameter-description":
				postData['actionkey'] = controllable.closest(".action").find(".controllable[data-componenttype=action-name]").data("actionkey");
				postData['parameterkey'] = controllable.data("parameterkey");
				console.log("parameter");
				break;
		}

		console.log(postData['actionkey']);
		console.log(postData['parameterkey']);
		console.log(postData);
		
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