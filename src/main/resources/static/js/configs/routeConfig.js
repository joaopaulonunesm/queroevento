angular.module("queroEventoApp").config(function ($routeProvider) {
	
	$routeProvider.when("/", {
		templateUrl: "views/index/home.html"
	});
		
	$routeProvider.otherwise({redirectTo: "/"});
	
});