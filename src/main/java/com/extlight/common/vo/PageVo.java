package com.extlight.common.vo;

/**
 *
 */
public class PageVo {

    private int pageNum;

    private int pageSize;

    private int total;

    private int pages;

    private Object data;

    private int[] navigatepageNums;

    private int navigatePages = 10;

    private boolean hasPreviousPage;

    private boolean hasNextPage;

    public PageVo(int pageNum, int pageSize, int total, Object data) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.data = data;

        this.setTotalPage();
        this.setNavigatepageNums();
    }

    public int getPageNum() {
        return pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotal() {
        return total;
    }

    public int getPages() {
        return pages;
    }

    public Object getData() {
        return data;
    }

    private void setTotalPage() {
        if (this.total % this.pageSize == 0) {
            this.pages = this.total / this.pageSize;
        } else {
            this.pages = this.total / this.pageSize + 1;
        }
    }

    private void setNavigatepageNums() {
        int startpage;
        int endpage;
        int pagebar[];
        if (this.pages <= navigatePages) {
            pagebar = new int[this.pages];
            startpage = 1;
            endpage = pages;
        } else {
            pagebar = new int[10];
            startpage = this.pageNum - 4;
            endpage = this.pageNum + 5;

            if (startpage < 1) {
                startpage = 1;
                endpage = 10;
            }

            if (endpage > this.pages) {
                endpage = this.pages;
                startpage = this.pages - 9;
            }
        }

        int index = 0;
        for (int i = startpage; i <= endpage; i++) {
            pagebar[index++] = i;
        }

        this.navigatepageNums = pagebar;
    }

    public int[] getNavigatepageNums() {
        return navigatepageNums;
    }

    public boolean isHasPreviousPage() {
        return this.pageNum > 1;
    }


    public boolean isHasNextPage() {
        return this.pageNum < this.pages;
    }

}
