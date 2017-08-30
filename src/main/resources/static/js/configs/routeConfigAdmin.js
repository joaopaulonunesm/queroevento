angular.module("queroEventoApp").config(function ($routeProvider) {
	
	$routeProvider.when("/", {
		templateUrl: "views/admin/home.html"
	});

	$routeProvider.when("/event/insert", {
		templateUrl: "views/admin/component/novoEvento.html"
	});

	$routeProvider.when("/event/list", {
		templateUrl: "views/admin/component/eventos.html"
	});

	$routeProvider.when("/offer", {
		templateUrl: "views/admin/component/ofertas.html"
	});

	$routeProvider.when("/settings", {
		templateUrl: "views/admin/component/configuracao.html"
	});
		
	$routeProvider.otherwise({redirectTo: "/"});
	
});