package com.practice.app1.domain;

import org.springframework.web.util.UriComponentsBuilder;

public class SearchCondition {
    private Integer page = 1;
    private Integer pageSize = 10;
    // paged 와 pageSize 로 결정되기 때문에 필요없다. 있으면 계속 관리 필요...
//    private Integer offset = 0;
    private String keyword = "";
    private String option = "";

    // PageHandler 에 있는 것 보다 SearchCondition 에 있는게 적절하다.
    public String getQueryString(Integer page) {
        // ?page=1&pageSize=10&option=T&keyword="title"
        // UriComponentsBuilder -> 자동으로 쿼리 스트링을 만들어줌
        return UriComponentsBuilder.newInstance()
                .queryParam("page", page)
                .queryParam("pageSize", pageSize)
                .queryParam("option", option)
                .queryParam("keyword", keyword)
                .build().toString();
    }

    // 위에꺼랑 메서드가 같아서 위에이쓴 메서드를 받아서 사용
    public String getQueryString(){
        return getQueryString(page);
    }

    public SearchCondition(){}
    public SearchCondition(Integer page, Integer pageSize, String keyword, String option) {
        this.page = page;
        this.pageSize = pageSize;
        this.keyword = keyword;
        this.option = option;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getOffset() {
        return (page-1)*pageSize;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    @Override
    public String toString() {
        return "SearchCondition{" +
                "page=" + page +
                ", pageSize=" + pageSize +
                ", offset=" + getOffset() +
                ", keyword='" + keyword + '\'' +
                ", option='" + option + '\'' +
                '}';
    }
}
