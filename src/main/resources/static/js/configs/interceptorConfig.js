angular.module("queroEventoApp").config(function ($httpProvider) {

	$httpProvider.interceptors.push("tokenInterceptor");

});