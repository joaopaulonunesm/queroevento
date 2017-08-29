angular.module("queroEventoApp").config(function ($routeProvider) {
	
	$routeProvider.when("/", {
		templateUrl: "views/admin/home.html"
	});
		
	$routeProvider.otherwise({redirectTo: "/"});
	
});