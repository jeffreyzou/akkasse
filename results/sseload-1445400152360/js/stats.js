var stats = {
    type: "GROUP",
name: "Global Information",
path: "",
pathFormatted: "group_missing-name-b06d1",
stats: {
    "name": "Global Information",
    "numberOfRequests": {
        "total": "2000",
        "ok": "2000",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "0",
        "ok": "0",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "500016",
        "ok": "500016",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "249876",
        "ok": "249876",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "249876",
        "ok": "249876",
        "ko": "-"
    },
    "percentiles1": {
        "total": "285412",
        "ok": "285412",
        "ko": "-"
    },
    "percentiles2": {
        "total": "499737",
        "ok": "499737",
        "ko": "-"
    },
    "percentiles3": {
        "total": "499942",
        "ok": "499942",
        "ko": "-"
    },
    "percentiles4": {
        "total": "499990",
        "ok": "499990",
        "ko": "-"
    },
    "group1": {
        "name": "t < 800 ms",
        "count": 1000,
        "percentage": 50
    },
    "group2": {
        "name": "800 ms < t < 1200 ms",
        "count": 0,
        "percentage": 0
    },
    "group3": {
        "name": "t > 1200 ms",
        "count": 1000,
        "percentage": 50
    },
    "group4": {
        "name": "failed",
        "count": 0,
        "percentage": 0
    },
    "meanNumberOfRequestsPerSecond": {
        "total": "3.955",
        "ok": "3.955",
        "ko": "-"
    }
},
contents: {
"req_streaminglogeve-3b5a0": {
        type: "REQUEST",
        name: "StreamingLogEvents",
path: "StreamingLogEvents",
pathFormatted: "req_streaminglogeve-3b5a0",
stats: {
    "name": "StreamingLogEvents",
    "numberOfRequests": {
        "total": "1000",
        "ok": "1000",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "499429",
        "ok": "499429",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "500016",
        "ok": "500016",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "499753",
        "ok": "499753",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "106",
        "ok": "106",
        "ko": "-"
    },
    "percentiles1": {
        "total": "499737",
        "ok": "499737",
        "ko": "-"
    },
    "percentiles2": {
        "total": "499791",
        "ok": "499791",
        "ko": "-"
    },
    "percentiles3": {
        "total": "499964",
        "ok": "499964",
        "ko": "-"
    },
    "percentiles4": {
        "total": "500000",
        "ok": "500000",
        "ko": "-"
    },
    "group1": {
        "name": "t < 800 ms",
        "count": 0,
        "percentage": 0
    },
    "group2": {
        "name": "800 ms < t < 1200 ms",
        "count": 0,
        "percentage": 0
    },
    "group3": {
        "name": "t > 1200 ms",
        "count": 1000,
        "percentage": 100
    },
    "group4": {
        "name": "failed",
        "count": 0,
        "percentage": 0
    },
    "meanNumberOfRequestsPerSecond": {
        "total": "1.977",
        "ok": "1.977",
        "ko": "-"
    }
}
    },"req_closesse-2c1bb": {
        type: "REQUEST",
        name: "CloseSSE",
path: "CloseSSE",
pathFormatted: "req_closesse-2c1bb",
stats: {
    "name": "CloseSSE",
    "numberOfRequests": {
        "total": "1000",
        "ok": "1000",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "0",
        "ok": "0",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "1",
        "ok": "1",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "0",
        "ok": "0",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "0",
        "ok": "0",
        "ko": "-"
    },
    "percentiles1": {
        "total": "0",
        "ok": "0",
        "ko": "-"
    },
    "percentiles2": {
        "total": "0",
        "ok": "0",
        "ko": "-"
    },
    "percentiles3": {
        "total": "0",
        "ok": "0",
        "ko": "-"
    },
    "percentiles4": {
        "total": "0",
        "ok": "0",
        "ko": "-"
    },
    "group1": {
        "name": "t < 800 ms",
        "count": 1000,
        "percentage": 100
    },
    "group2": {
        "name": "800 ms < t < 1200 ms",
        "count": 0,
        "percentage": 0
    },
    "group3": {
        "name": "t > 1200 ms",
        "count": 0,
        "percentage": 0
    },
    "group4": {
        "name": "failed",
        "count": 0,
        "percentage": 0
    },
    "meanNumberOfRequestsPerSecond": {
        "total": "1.977",
        "ok": "1.977",
        "ko": "-"
    }
}
    }
}

}

function fillStats(stat){
    $("#numberOfRequests").append(stat.numberOfRequests.total);
    $("#numberOfRequestsOK").append(stat.numberOfRequests.ok);
    $("#numberOfRequestsKO").append(stat.numberOfRequests.ko);

    $("#minResponseTime").append(stat.minResponseTime.total);
    $("#minResponseTimeOK").append(stat.minResponseTime.ok);
    $("#minResponseTimeKO").append(stat.minResponseTime.ko);

    $("#maxResponseTime").append(stat.maxResponseTime.total);
    $("#maxResponseTimeOK").append(stat.maxResponseTime.ok);
    $("#maxResponseTimeKO").append(stat.maxResponseTime.ko);

    $("#meanResponseTime").append(stat.meanResponseTime.total);
    $("#meanResponseTimeOK").append(stat.meanResponseTime.ok);
    $("#meanResponseTimeKO").append(stat.meanResponseTime.ko);

    $("#standardDeviation").append(stat.standardDeviation.total);
    $("#standardDeviationOK").append(stat.standardDeviation.ok);
    $("#standardDeviationKO").append(stat.standardDeviation.ko);

    $("#percentiles1").append(stat.percentiles1.total);
    $("#percentiles1OK").append(stat.percentiles1.ok);
    $("#percentiles1KO").append(stat.percentiles1.ko);

    $("#percentiles2").append(stat.percentiles2.total);
    $("#percentiles2OK").append(stat.percentiles2.ok);
    $("#percentiles2KO").append(stat.percentiles2.ko);

    $("#percentiles3").append(stat.percentiles3.total);
    $("#percentiles3OK").append(stat.percentiles3.ok);
    $("#percentiles3KO").append(stat.percentiles3.ko);

    $("#percentiles4").append(stat.percentiles4.total);
    $("#percentiles4OK").append(stat.percentiles4.ok);
    $("#percentiles4KO").append(stat.percentiles4.ko);

    $("#meanNumberOfRequestsPerSecond").append(stat.meanNumberOfRequestsPerSecond.total);
    $("#meanNumberOfRequestsPerSecondOK").append(stat.meanNumberOfRequestsPerSecond.ok);
    $("#meanNumberOfRequestsPerSecondKO").append(stat.meanNumberOfRequestsPerSecond.ko);
}
