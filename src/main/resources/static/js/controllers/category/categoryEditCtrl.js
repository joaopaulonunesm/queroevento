angular.module("queroEventoApp").controller("categoryEditCtrl", function ($scope, $http, categoryAPI, $routeParams, $location) {

	$scope.category = {};

	$scope.getCategory = function(url) {
		
		categoryAPI.getCategoryByUrlName(url).then(function(response) {
			
			$scope.category = response.data;
			
		}, function(response) {
			console.log(response.data);
			console.log(response.status);
		});

	};

	$scope.getCategory($routeParams.url);
	
	$scope.updateCategory = function(url) {

		categoryAPI.putCategory(url, $scope.category).then(function(response) {

			$scope.category = {};
			$location.path("/category/list");

		}, function(response) {
			console.log(response.data);
			console.log(response.status);
		});
	};

});