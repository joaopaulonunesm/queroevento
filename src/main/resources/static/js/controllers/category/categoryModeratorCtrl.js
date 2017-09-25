angular.module("queroEventoApp").controller("categoryModeratorCtrl", function ($scope, $location, categoryAPI) {

	$scope.categories = [];
	
	$scope.category = {};
	
	$scope.getCategories = function() {

		categoryAPI.getCategories().then(function(response) {

			$scope.categories = response.data;

		}, function(response) {
			console.log(response.data);
			console.log(response.status);
		});
	};
	
	$scope.getCategories();
	
	$scope.createCategory = function() {

		categoryAPI.postCategory($scope.category).then(function(response) {

			$scope.category = {};
			$scope.getCategories();
			$location.path("/category/list");

		}, function(response) {
			console.log(response.data);
			console.log(response.status);
		});
	};
	
	$scope.deleteCategory = function(id) {

		categoryAPI.deleteCategory(id).then(function(response) {

			$scope.getCategories();

		}, function(response) {
			console.log(response.data);
			console.log(response.status);
		});
	};
	
});