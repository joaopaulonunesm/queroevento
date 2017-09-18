angular.module("queroEventoApp").config(function ($routeProvider) {
	
	$routeProvider.when("/", {
		templateUrl: "views/admin/home.html"
	});
	
	$routeProvider.when("/login", {
		templateUrl: "views/admin/login.html"
	});

	$routeProvider.when("/event/insert", {
		templateUrl: "views/admin/component/novoEvento.html"
	});

	$routeProvider.when("/event/list", {
		templateUrl: "views/admin/component/eventos.html",
		controller: "adminEventCtrl"
	});
	
	$routeProvider.when("/event/edit/:url", {
		templateUrl: "views/admin/component/editarEvento.html",
		controller: "adminEditEventCtrl"
	});

	$routeProvider.when("/event/preview/:url", {
		templateUrl: "views/admin/component/visualizarEvento.html",
		controller: "adminEditEventCtrl"
	});
	
	$routeProvider.when("/offer", {
		templateUrl: "views/admin/component/ofertas.html"
	});

	$routeProvider.when("/settings", {
		templateUrl: "views/admin/component/configuracao.html"
	});

	$routeProvider.when("/profile/:nameUrl", {
		templateUrl: "views/admin/component/perfil.html",
		controller: "eventByCompanyCtrl"
	});
		
	$routeProvider.otherwise({redirectTo: "/"});
	
});