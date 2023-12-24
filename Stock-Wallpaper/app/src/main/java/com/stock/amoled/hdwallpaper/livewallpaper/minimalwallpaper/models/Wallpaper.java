package com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Wallpaper implements Serializable {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Detail> data = null;
    @SerializedName("meta")
    @Expose
    private Meta meta;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Detail> getDetail() {
        return data;
    }

    public void setDetail(List<Detail> data) {
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public static class Detail implements Serializable {

        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("category")
        @Expose
        private List<Category> category = null;
        @SerializedName("colour")
        @Expose
        private List<Colour> colour = null;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("size")
        @Expose
        private Double size;
        @SerializedName("thumbUrl")
        @Expose
        private String thumbUrl;
        @SerializedName("downloadCount")
        @Expose
        private Integer downloadCount;
        @SerializedName("isLiveWallpaper")
        @Expose
        private Boolean isLiveWallpaper;
        @SerializedName("resolution")
        @Expose
        private Resolution resolution;
        @SerializedName("isFromAPI")
        @Expose
        private boolean isFromAPI = false;
        private boolean isFromAssets = false;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<Category> getCategory() {
            return category;
        }

        public void setCategory(List<Category> category) {
            this.category = category;
        }

        public List<Colour> getColour() {
            return colour;
        }

        public void setColour(List<Colour> colour) {
            this.colour = colour;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public Double getSize() {
            return size;
        }

        public void setSize(Double size) {
            this.size = size;
        }

        public String getThumbUrl() {
            return thumbUrl;
        }

        public void setThumbUrl(String thumbUrl) {
            this.thumbUrl = thumbUrl;
        }

        public Integer getDownloadCount() {
            return downloadCount;
        }

        public void setDownloadCount(Integer downloadCount) {
            this.downloadCount = downloadCount;
        }

        public Resolution getResolution() {
            return resolution;
        }

        public void setResolution(Resolution resolution) {
            this.resolution = resolution;
        }

        public Boolean getLiveWallpaper() {
            return isLiveWallpaper;
        }

        public void setLiveWallpaper(Boolean liveWallpaper) {
            isLiveWallpaper = liveWallpaper;
        }

        public boolean isFromAPI() {
            return isFromAPI;
        }

        public void setFromAPI(boolean fromAPI) {
            isFromAPI = fromAPI;
        }

        public boolean isFromAssets() {
            return isFromAssets;
        }

        public void setFromAssets(boolean fromAssets) {
            isFromAssets = fromAssets;
        }

        public static class Category implements Serializable {

            @SerializedName("_id")
            @Expose
            private String id;
            @SerializedName("name")
            @Expose
            private String name;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class Colour implements Serializable {

            @SerializedName("_id")
            @Expose
            private String id;
            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("hashCode")
            @Expose
            private String hashCode;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getHashCode() {
                return hashCode;
            }

            public void setHashCode(String hashCode) {
                this.hashCode = hashCode;
            }
        }

        public static class Resolution implements Serializable {

            @SerializedName("width")
            @Expose
            private String width;
            @SerializedName("height")
            @Expose
            private String height;

            public String getWidth() {
                return width;
            }

            public void setWidth(String width) {
                this.width = width;
            }

            public String getHeight() {
                return height;
            }

            public void setHeight(String height) {
                this.height = height;
            }
        }
    }

    public static class Meta {

        @SerializedName("pageNumber")
        @Expose
        private Integer pageNumber;
        @SerializedName("pageSize")
        @Expose
        private Integer pageSize;
        @SerializedName("totalCount")
        @Expose
        private Integer totalCount;
        @SerializedName("prevPage")
        @Expose
        private Boolean prevPage;
        @SerializedName("nextPage")
        @Expose
        private Boolean nextPage;
        @SerializedName("totalPages")
        @Expose
        private Integer totalPages;

        public Integer getPageNumber() {
            return pageNumber;
        }

        public void setPageNumber(Integer pageNumber) {
            this.pageNumber = pageNumber;
        }

        public Integer getPageSize() {
            return pageSize;
        }

        public void setPageSize(Integer pageSize) {
            this.pageSize = pageSize;
        }

        public Integer getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(Integer totalCount) {
            this.totalCount = totalCount;
        }

        public Boolean getPrevPage() {
            return prevPage;
        }

        public void setPrevPage(Boolean prevPage) {
            this.prevPage = prevPage;
        }

        public Boolean getNextPage() {
            return nextPage;
        }

        public void setNextPage(Boolean nextPage) {
            this.nextPage = nextPage;
        }

        public Integer getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(Integer totalPages) {
            this.totalPages = totalPages;
        }

    }
}
