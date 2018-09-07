angular.module("queroEventoApp").factory("categoryAPI", function ($http, configs){

	var _postCategory = function (category){
		return $http.post(configs.baseUrl + configs.version + '/categories', category);
	}
	
	var _deleteCategory = function (id){
		return $http.delete(configs.baseUrl + configs.version + '/categories/' + id);
	}

	var _putCategory = function (urlName, category){
		return $http.put(configs.baseUrl + configs.version + '/categories/' + urlName, category);
	}
	
	var _getCategoryByUrlName = function (urlName){
		return $http.get(configs.baseUrl + '/categories/' + urlName);
	}
	
	var _getCategoriesGreaterThanZero = function (){
		return $http.get(configs.baseUrl + '/categories/greaterthanzero');
	}

	var _getCategories = function (){
		return $http.get(configs.baseUrl + '/categories');
	}

	return {
		postCategory: _postCategory,
		deleteCategory: _deleteCategory,
		putCategory: _putCategory,
		getCategoryByUrlName: _getCategoryByUrlName,
		getCategories: _getCategories,
		getCategoriesGreaterThanZero: _getCategoriesGreaterThanZero
    };
	
});