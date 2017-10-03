var app = angular.module('myAngularApp', []);
app.controller('myController', function ($scope,$window,$http) {
    $scope.message = "Hello World!";
    $scope.DisplayMessage = function (value) {
        $window.alert(value)
    }
    var onSuccess = function (data, status, headers, config) {
        $scope.data = data;
    };

    var onError = function (data, status, headers, config) {
        $scope.error = status;
    }
    var promise = $http.post("http://lnxdevvm658:8180/connect/res/sales/search/getAirlineList").success(onSuccess).error(onError);





});