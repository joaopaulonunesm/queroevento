angular.module("queroEventoApp").config(function ($routeProvider) {
	
	$routeProvider.when("/", {
		templateUrl: "views/moderator/component/dashboard.html"
	});
	
	$routeProvider.when("/events", {
		templateUrl: "views/moderator/component/evento.html"
	});
	
	$routeProvider.when("/category/list", {
		templateUrl: "views/moderator/component/categoria.html",
		controller: "categoryModeratorCtrl"
	});
	
	$routeProvider.when("/category/insert", {
		templateUrl: "views/moderator/component/novaCategoria.html",
		controller: "categoryModeratorCtrl"
	});
	
	$routeProvider.when("/category/edit/:url", {
		templateUrl: "views/moderator/component/editarCategoria.html",
		controller: "categoryEditCtrl"
	});
	
	$routeProvider.when("/offers", {
		templateUrl: "views/moderator/component/ofertas.html"
	});
	
	$routeProvider.when("/settings", {
		templateUrl: "views/moderator/component/configuracao.html"
	});
		
	$routeProvider.otherwise({redirectTo: "/"});
	
});