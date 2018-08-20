package com.xht.androidnote.bean.retrofit;

import java.util.List;

/**
 * Created by xht on 2018/5/17.
 */

public class CourseBean {

    /**
     * data : [{"avg_score":"0.56820","bl_id":"aa3cf1e09ef3ed2b08d293029d5adbb7_a","course_type":"1","ctime":"1454491214","descp":" 作者采用丰富的想象、拟人和比喻的修辞手法表达了对草虫和大自然的喜爱之情","duration":"05:50","grade":"6","img_url":"/Uploads/Picture/2015-09-29/560a340fc8b97.jpg","is_recommend":"1","price":"1.00","resource_id":"300","shares":"164","term":"1","title":"草虫的村落"}]
     * note : 42
     * status : 1
     */

    private int note;
    private int status;
    private List<DataBean> data;

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CourseBean{" +
                "note=" + note +
                ", status=" + status +
                ", data=" + data +
                '}';
    }

    public static class DataBean {
        /**
         * avg_score : 0.56820
         * bl_id : aa3cf1e09ef3ed2b08d293029d5adbb7_a
         * course_type : 1
         * ctime : 1454491214
         * descp :  作者采用丰富的想象、拟人和比喻的修辞手法表达了对草虫和大自然的喜爱之情
         * duration : 05:50
         * grade : 6
         * img_url : /Uploads/Picture/2015-09-29/560a340fc8b97.jpg
         * is_recommend : 1
         * price : 1.00
         * resource_id : 300
         * shares : 164
         * term : 1
         * title : 草虫的村落
         */

        private String avg_score;
        private String bl_id;
        private String course_type;
        private String ctime;
        private String descp;
        private String duration;
        private String grade;
        private String img_url;
        private String is_recommend;
        private String price;
        private String resource_id;
        private String shares;
        private String term;
        private String title;

        public String getAvg_score() {
            return avg_score;
        }

        public void setAvg_score(String avg_score) {
            this.avg_score = avg_score;
        }

        public String getBl_id() {
            return bl_id;
        }

        public void setBl_id(String bl_id) {
            this.bl_id = bl_id;
        }

        public String getCourse_type() {
            return course_type;
        }

        public void setCourse_type(String course_type) {
            this.course_type = course_type;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public String getDescp() {
            return descp;
        }

        public void setDescp(String descp) {
            this.descp = descp;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getIs_recommend() {
            return is_recommend;
        }

        public void setIs_recommend(String is_recommend) {
            this.is_recommend = is_recommend;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getResource_id() {
            return resource_id;
        }

        public void setResource_id(String resource_id) {
            this.resource_id = resource_id;
        }

        public String getShares() {
            return shares;
        }

        public void setShares(String shares) {
            this.shares = shares;
        }

        public String getTerm() {
            return term;
        }

        public void setTerm(String term) {
            this.term = term;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "avg_score='" + avg_score + '\'' +
                    ", bl_id='" + bl_id + '\'' +
                    ", course_type='" + course_type + '\'' +
                    ", ctime='" + ctime + '\'' +
                    ", descp='" + descp + '\'' +
                    ", duration='" + duration + '\'' +
                    ", grade='" + grade + '\'' +
                    ", img_url='" + img_url + '\'' +
                    ", is_recommend='" + is_recommend + '\'' +
                    ", price='" + price + '\'' +
                    ", resource_id='" + resource_id + '\'' +
                    ", shares='" + shares + '\'' +
                    ", term='" + term + '\'' +
                    ", title='" + title + '\'' +
                    '}';
        }
    }
}
