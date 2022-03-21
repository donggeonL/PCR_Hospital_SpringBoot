package com.example.pcr_search.batch;

// 일주일에 한번씩 다운로드해서 DB에 변경해주기
// pcr 검사기관이 추가 될 수 있기 때문에!
// 데이터를 삭제하고 다시 추가하는 방식!
// 공공데이터 트래픽이 1000이라 바로바로 서비스 해주는 방식은 어려움!

import com.example.pcr_search.domain.Hospital;
import com.example.pcr_search.domain.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class HospDownloadBatch {

    //DI
    private final HospitalRepository hospitalRepository;
    //초 분 시 일 월 주
    @Scheduled(cron = "0 29 * * * *", zone = "Asia/Seoul") //매 시간마다 배치함
    public void startBatch(){

        //1. 담을 그릇 준비
        List<Hospital> hospitals = new ArrayList<>();

        //2. api 한번 호출해서 totalcount 확인

        RestTemplate rt = new RestTemplate();
        //serviceKey 인코딩 문제로 URI 객체로 감싸야한다
        //사이즈를 1로 했더니 item이 컬렉션이 아니라서 파싱이 안되서 2로 바꿈
        int totalCount = 2;
        String url = "http://apis.data.go.kr/B551182/rprtHospService/getRprtHospService?serviceKey=tzoFe2PJS55%2FBfRhEvfC8JBK7KNrxnPfJ1Gisq%2FeX7bo%2B%2FjMzT9tMNKS7D%2BhmvjYmmkjoNG%2F2BEVKA3h093OjA%3D%3D&pageNo=1&numOfRows="+totalCount+"&_type=json";
        URI uri = null;

        try{
            uri = new URI(url);
            ResponseDto totalCountDto = rt.getForObject(uri,ResponseDto.class);
            totalCount = totalCountDto.getResponse().getBody().getTotalCount();

            url = "http://apis.data.go.kr/B551182/rprtHospService/getRprtHospService?serviceKey=tzoFe2PJS55%2FBfRhEvfC8JBK7KNrxnPfJ1Gisq%2FeX7bo%2B%2FjMzT9tMNKS7D%2BhmvjYmmkjoNG%2F2BEVKA3h093OjA%3D%3D&pageNo=1&numOfRows="+totalCount+"&_type=json";
            uri = new URI(url);

            //3. totalCount 만큼 한번에 가져오기 totalCount = 5136
            ResponseDto responseDto = rt.getForObject(uri,ResponseDto.class);

            List<Item> items = responseDto.getResponse().getBody().getItems().getItem();
            System.out.println("가져온 데이터 사이즈 : "+ items.size());

            hospitals = items.stream()
                    .map(item -> Hospital.builder()
                            .addr(item.getAddr())
                            .mgtStaDd(item.getMgtStaDd())
                            .pcrPsblYn(item.getPcrPsblYn())
                            .ratPsblYn(item.getRatPsblYn())
                            .recuClCd(item.getRecuClCd())
                            .rnum(item.getRnum())
                            .rprtWorpClicFndtTgtYn(item.getRprtWorpClicFndtTgtYn())
                            .sgguCdNm(item.getSgguCdNm())
                            .sidoCdNm(item.getSidoCdNm())
                            .telno(item.getTelno())
                            .xPosWgs84(item.getXPosWgs84())
                            .yPosWgs84(item.getYPosWgs84())
                            .yadmNm(item.getYadmNm())
                            .ykihoEnc(item.getYkihoEnc())
                            .xPos(item.getXPos())
                            .yPos(item.getYPos())
                            .build()
                    )
                    .collect(Collectors.toList());
            //기존 데이터 다 삭제하기 (삭제 잘되는지 먼저 테스트 하기 위해 yml - ddl-auto - update로 변경
            hospitalRepository.deleteAll();

            //배치시간에 DB에 INSERT 하기 (하루에 한번 할 예정 우선 테스트 지금은 23 분
            hospitalRepository.saveAll(hospitals);
        }catch (Exception e){
            e.printStackTrace();
        }
        //System.out.println("1분 마다 실행됨");
    }

}