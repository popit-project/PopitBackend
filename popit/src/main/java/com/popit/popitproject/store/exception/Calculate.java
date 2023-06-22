package com.popit.popitproject.store.exception;

public class Calculate {
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // 지구의 반지름 (단위: km)
        double earthRadius = 6371;

        // 두 지점의 위도 및 경도를 라디안으로 변환
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // Haversine 공식을 사용하여 두 지점 사이의 거리 계산
        double dlon = lon2Rad - lon1Rad;
        double dlat = lat2Rad - lat1Rad;
        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;

        return distance;
    }
}
