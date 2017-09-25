angular.module("queroEventoApp").controller("categoryCtrl", function ($scope, $location, categoryAPI) {

	$scope.categories = [];
	
	$scope.getCategories = function() {

		categoryAPI.getCategoriesGreaterThanZero().then(function(response) {

			$scope.categories = response.data;

		}, function(response) {
			console.log(response.data);
			console.log(response.status);
		});
	};
	
	$scope.getCategories();
	
	setInterval($scope.getCategories, 100000);
	
});