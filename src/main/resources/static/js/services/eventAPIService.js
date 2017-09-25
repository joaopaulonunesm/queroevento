angular.module("queroEventoApp").factory("eventAPI", function ($http, configs){

	var _postEvent = function (event){
		return $http.post(configs.baseUrl + configs.version + '/events', event);
	}
	
	var _putEvent = function (urlTitle, event){
		return $http.put(configs.baseUrl + configs.version + '/events/' + urlTitle, event);
	}
	
	var _putEventEstimate = function (id){
		return $http.put(configs.baseUrl + configs.version + '/events/' + id + '/estimate');
	}
	
	var _putEventStatus = function (id, event){
		return $http.put(configs.baseUrl + configs.version + '/events/' + id + '/status', event);
	}
	
	var _putEventCatalogStatus = function (id, event){
		return $http.put(configs.baseUrl + configs.version + '/events/' + id + '/status/catalog', event);
	}
	
	var _putEventTurbineType = function (id, event){
		return $http.put(configs.baseUrl + configs.version + '/events/' + id + '/turbine', event);
	}
	
	var _deleteEvent = function (id){
		return $http.delete(configs.baseUrl + configs.version + '/events/' + id);
	}
	
	var _getEventByCatalogStatusPending = function (){
		return $http.get(configs.baseUrl + configs.version + '/events/status/catalog/pending');
	}
	
	var _getEventByCatalogStatusRefused = function (){
		return $http.get(configs.baseUrl + configs.version + '/events/status/catalog/refused');
	}
	
	var _getEventByStatusCanceled = function (){
		return $http.get(configs.baseUrl + configs.version + '/events/status/canceled');
	}
	
	var _getEventByKeyword = function (keyword){
		return $http.get(configs.baseUrl + configs.version + '/events/keyword/' + keyword);
	}
	
	var _getEventByCompany = function (){
		return $http.get(configs.baseUrl + configs.version + '/events/company');
	}
	
	var _getEventById = function (id){
		return $http.get(configs.baseUrl + '/events/' + id);
	}
	
	var _getEvents = function (){
		return $http.get(configs.baseUrl + '/events');
	}
	
	var _getEventsPast = function (){
		return $http.get(configs.baseUrl + '/events/past');
	}
	
	var _getEventsByCategory = function (url){
		return $http.get(configs.baseUrl + '/events/category/' + url);
	}
	
	var _getEventsOrderByEstimate = function (){
		return $http.get(configs.baseUrl + '/events/estimate');
	}
	
	var _getEventByUrlTitle = function (urlTitle){
		return $http.get(configs.baseUrl + '/events/urltitle/' + urlTitle);
	}
	
	var _getEventsGold = function (){
		return $http.get(configs.baseUrl  + '/events/type/turbine/gold');
	}
	
	var _getEventsSilver = function (){
		return $http.get(configs.baseUrl  + '/events/type/turbine/silver');
	}
	
	var _getEventsBronze = function (){
		return $http.get(configs.baseUrl  + '/events/type/turbine/bronze');
	}

	var _getEventsByWord = function (word){
		return $http.get(configs.baseUrl  + '/events/search/' + word);
	}
	
	var _getEventsByCompanyUrl = function (url){
		return $http.get(configs.baseUrl  + '/events/company/' + url);
	}

	return {
    	postEvent: _postEvent,
    	putEvent: _putEvent,
    	putEventEstimate: _putEventEstimate,
    	putEventStatus: _putEventStatus,
    	putEventCatalogStatus: _putEventCatalogStatus,
    	putEventTurbineType: _putEventTurbineType,
    	deleteEvent: _deleteEvent,
    	getEventById: _getEventById,
    	getEvents: _getEvents,
    	getEventsPast: _getEventsPast,
    	getEventsByCategory: _getEventsByCategory,
    	getEventsOrderByEstimate: _getEventsOrderByEstimate,
    	getEventByUrlTitle: _getEventByUrlTitle,
    	getEventByCatalogStatusPending: _getEventByCatalogStatusPending,
    	getEventByCatalogStatusRefused: _getEventByCatalogStatusRefused,
    	getEventByStatusCanceled: _getEventByStatusCanceled,
    	getEventByKeyword: _getEventByKeyword,
    	getEventByCompany: _getEventByCompany,
    	getEventsGold: _getEventsGold,
    	getEventsSilver: _getEventsSilver,
    	getEventsBronze: _getEventsBronze,
    	getEventsByWord: _getEventsByWord,
    	getEventsByCompanyUrl: _getEventsByCompanyUrl
    };
	
});