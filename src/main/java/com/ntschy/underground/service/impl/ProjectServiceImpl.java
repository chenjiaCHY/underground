/*
 * Copyright (c) 2021. All Rights Reserved.
 * ProjectName: underground
 * FileName: ProjectServiceImpl.java
 * Author: 陈佳
 * Date: 2021/8/19 下午2:58
 * Version: 1.0
 * LastModified
 *
 */

package com.ntschy.underground.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ntschy.underground.dao.ProjectDao;
import com.ntschy.underground.entity.DO.InspectionRecord;
import com.ntschy.underground.entity.DO.ProjectPoint;
import com.ntschy.underground.entity.DO.ProjectRecord;
import com.ntschy.underground.entity.DO.RectificationRecord;
import com.ntschy.underground.entity.ShapePoint;
import com.ntschy.underground.entity.base.PageQuery;
import com.ntschy.underground.entity.base.Result;
import com.ntschy.underground.entity.dto.*;
import com.ntschy.underground.entity.vo.InspectionVO;
import com.ntschy.underground.entity.vo.RectificationVO;
import com.ntschy.underground.enums.InspectionType;
import com.ntschy.underground.enums.ProgressType;
import com.ntschy.underground.enums.UploadFileType;
import com.ntschy.underground.service.ProjectService;
import com.ntschy.underground.utils.ToolUpload;
import com.ntschy.underground.utils.Utils;
import org.apache.cxf.endpoint.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.xml.namespace.QName;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Resource
    private ProjectDao projectDao;

    @Value("${file.uploadPath}")
    private String uploadPath;

    @Value("${webservice.url}")
    private String webServiceUrl;

    @Value("${webservice.namespace}")
    private String nameSpace;

    @Value("${webservice.pctopad}")
    private String pcToPad;

    @Value("${webservice.padtopc}")
    private String padToPc;

    @Autowired
    Client client;

    /**
     * 新建项目
     * @param addProjectRequest
     * @return
     * @throws RuntimeException
     */
    @Override
    public Result addProject(AddProjectRequest addProjectRequest) throws RuntimeException {

        String projectId = Utils.GenerateUUID(32);

        // 转换点坐标并存到PROJECT_POINTS表
        List<ShapePoint> points = addProjectRequest.getPoints();
        if (CollectionUtils.isEmpty(points)) {
            return new Result(false, "坐标点列表为空");
        }

        List<ProjectPoint> projectPointList = new ArrayList<>();
        int sort = 1;
        for (ShapePoint point : points) {
            String pid = Utils.GenerateUUID(32);
            ProjectPoint projectPoint = new ProjectPoint();
            projectPoint.setPid(pid);
            projectPoint.setGuid(addProjectRequest.getGuid());
            projectPoint.setX(point.getX());
            projectPoint.setY(point.getY());
            // 坐标转换
            Map<String, String> coordinateMap = coordinateConvert(point.getX(), point.getY(), "PCTOPAD");

            projectPoint.setXt(coordinateMap.get("x"));
            projectPoint.setYt(coordinateMap.get("y"));
            projectPoint.setSort(sort);
            projectPoint.setGeoType(addProjectRequest.getShapeType().getCode());
            sort ++;
            projectPointList.add(projectPoint);
        }

        // 一次性插入到PROJECT_POINTS表
        projectDao.insertProjectPoints(projectPointList);

        // 插入一条记录到PROJECT表中
        ProjectRecord projectRecord = new ProjectRecord();

        projectRecord.setProjectId(projectId);
        projectRecord.setProjectScope(addProjectRequest.getProjectScope());
        projectRecord.setProjectName(addProjectRequest.getProjectName());
        projectRecord.setCreateTime(addProjectRequest.getCreateTime());
        projectRecord.setCreateUser(addProjectRequest.getCreateUser());
        projectRecord.setGuid(addProjectRequest.getGuid());
        projectRecord.setShapeType(addProjectRequest.getShapeType().getCode());

        projectDao.addProject(projectRecord);

        // 将图纸文件名列表插入到FILE_UPLOAD表中
        projectDao.addFiles(UploadFileType.PROJECT.getCode(), projectId, addProjectRequest.getFileNames());

        return new Result(true, "新增项目成功!!!");
    }

    /**
     * 新增巡检
     * @param addInspectionRequest
     * @return
     * @throws RuntimeException
     */
    @Override
    public Result addInspection(AddInspectionRequest addInspectionRequest, MultipartFile[] files) throws RuntimeException {

        String inspectionId = Utils.GenerateUUID(32);

        // 上传照片
        List<String> fileNames = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = ToolUpload.fileUpload2(file, uploadPath);
            if (!StringUtils.isEmpty(fileName)) {
                fileNames.add(fileName);
            }
        }

        SimpleDateFormat sortFormat = new SimpleDateFormat("yyMMddHHmmss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();

        // 插入一条记录到INSPECTION表中
        InspectionRecord inspectionRecord = new InspectionRecord();

        inspectionRecord.setInspectionId(inspectionId);
        inspectionRecord.setCreateTime(dateFormat.format(currentDate));
        inspectionRecord.setInspector(addInspectionRequest.getInspector());
        inspectionRecord.setPhone(addInspectionRequest.getPhone());
        inspectionRecord.setType(addInspectionRequest.getInspectionType().getCode());
        inspectionRecord.setProjectName(addInspectionRequest.getProjectName());
        inspectionRecord.setAddress(addInspectionRequest.getAddress());
        inspectionRecord.setDescription(addInspectionRequest.getDescription());
        inspectionRecord.setProgress(ProgressType.NOT_REVIEW.getCode());
        inspectionRecord.setSort(sortFormat.format(currentDate));
        inspectionRecord.setXt(addInspectionRequest.getXt());
        inspectionRecord.setYt(addInspectionRequest.getYt());
        // 转换下坐标
        Map<String, String> coordinateMap = coordinateConvert(addInspectionRequest.getXt(), addInspectionRequest.getYt(), "TDTTOPC");
        inspectionRecord.setX(coordinateMap.get("x"));
        inspectionRecord.setY(coordinateMap.get("y"));

        projectDao.addInspection(inspectionRecord);

        // 将巡检照片文件名列表插入到FILE_UPLOAD表中
        projectDao.addFiles(UploadFileType.INSPECTION.getCode(), inspectionId, fileNames);

        return new Result(true, "新增巡检成功!!!");
    }

    /**
     * 新增整改
     * @param addRectificationRequest
     * @return
     * @throws RuntimeException
     */
    @Override
    public Result addRectification(AddRectificationRequest addRectificationRequest, MultipartFile[] files) throws RuntimeException {

        String rectificationId = Utils.GenerateUUID(32);

        // 上传照片
        List<String> fileNames = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = ToolUpload.fileUpload2(file, uploadPath);
            if (!StringUtils.isEmpty(fileName)) {
                fileNames.add(fileName);
            }
        }

        SimpleDateFormat sortFormat = new SimpleDateFormat("yyMMddHHmmss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date currentDate = new Date();

        // 插入一条记录到INSPECTION表中
        RectificationRecord rectificationRecord = new RectificationRecord();

        rectificationRecord.setRectificationId(rectificationId);
        rectificationRecord.setCreateTime(dateFormat.format(new Date()));
        rectificationRecord.setInspectionId(addRectificationRequest.getInspectionId());
        rectificationRecord.setDescription(addRectificationRequest.getDescription());
        rectificationRecord.setSort(sortFormat.format(currentDate));
        rectificationRecord.setRectifyUser(addRectificationRequest.getRectifyUser());

        projectDao.addRectification(rectificationRecord);

        // 将巡检照片文件名列表插入到FILE_UPLOAD表中
        projectDao.addFiles(UploadFileType.RECTIFICATION.getCode(), rectificationId, fileNames);

        return new Result(true, "新增整改成功!!!");
    }

    /**
     * 获取项目列表
     * @return
     */
    @Override
    public Result getProjectList() {
        List<ProjectRecord> projectRecordList = projectDao.getProjectList();
        projectRecordList = Optional.ofNullable(projectRecordList).orElse(Collections.emptyList());

        return new Result<>(projectRecordList);
    }

    /**
     * 获取项目详情
     * @param guid
     * @return
     */
    @Override
    public Result getProjectInfo(String guid) {

        ProjectRecord projectRecord = projectDao.getProjectInfo(guid);

        if (!ObjectUtils.isEmpty(projectRecord)) {
            List<String> fileNames = projectDao.getFiles(UploadFileType.PROJECT.getCode(), projectRecord.getProjectId());
            projectRecord.setFileNames(fileNames);
        } else {
            return new Result(false, "获取项目信息失败!!!");
        }

        return new Result<>(projectRecord);
    }

    /**
     * 增加项目图纸
     * @param addProjectFileRequest
     * @return
     * @throws RuntimeException
     */
    @Override
    public Result addProjectFiles(AddProjectFileRequest addProjectFileRequest) throws RuntimeException {

        // 先删除旧图纸
        projectDao.deleteFiles(UploadFileType.PROJECT.getCode(), addProjectFileRequest.getProjectId());

        // 再插入新图纸
        projectDao.addFiles(UploadFileType.PROJECT.getCode(), addProjectFileRequest.getProjectId(), addProjectFileRequest.getFileNames());

        return new Result(true, "增加图纸成功!!!");
    }

    /**
     * 查询巡检记录，pad用，不分页
     * @param queryInspectionRequest
     * @return
     * @throws RuntimeException
     */
    @Override
    public Result getInspectionList(QueryInspectionRequest queryInspectionRequest) throws RuntimeException {

        Integer progress = null;
        Integer type = null;

        if (queryInspectionRequest.getProgress() != null) {
            progress = queryInspectionRequest.getProgress().getCode();
        }
        if (queryInspectionRequest.getType() != null) {
            type = queryInspectionRequest.getType().getCode();
        }

        List<InspectionRecord> inspectionRecords = projectDao.getAllInspection(progress, queryInspectionRequest.getCreateTime(), type);

        // 如果没有查到，直接返回空
        if (CollectionUtils.isEmpty(inspectionRecords)) {
            return new Result(false, "未查到相关记录！");
        }

        List<InspectionVO> inspectionVOList = new ArrayList<>();

        // 先查询一下第一条记录的Sort值，比如210825165314，这个时候要给它编号的话，必须知道同一天该条记录前面还有多少条记录
        String endSort = inspectionRecords.get(0).getSort();
        String beginSort = endSort.substring(0, 6) + "000000";
        int index = projectDao.findCountBySort(beginSort, endSort) + 1;

        beginSort = beginSort.substring(0, 6);
        for (InspectionRecord inspectionRecord : inspectionRecords) {
            InspectionVO inspectionVO = new InspectionVO();

            inspectionVO.setAddress(inspectionRecord.getAddress());
            inspectionVO.setCreateTime(inspectionRecord.getCreateTime());
            inspectionVO.setDescription(inspectionRecord.getDescription());
            inspectionVO.setInspectionId(inspectionRecord.getInspectionId());
            inspectionVO.setInspector(inspectionRecord.getInspector());
            inspectionVO.setPhone(inspectionRecord.getPhone());
            inspectionVO.setProgress(ProgressType.getName(inspectionRecord.getProgress()));
            inspectionVO.setProjectName(inspectionRecord.getProjectName());
            inspectionVO.setType(InspectionType.getName(inspectionRecord.getType()));
            inspectionVO.setRectifyComment(inspectionRecord.getRectifyComment());
            inspectionVO.setX(inspectionRecord.getX());
            inspectionVO.setY(inspectionRecord.getY());
            inspectionVO.setXt(inspectionRecord.getXt());
            inspectionVO.setYt(inspectionRecord.getYt());


            String sortDate = inspectionRecord.getSort().substring(0, 6);

            if (!beginSort.equals(sortDate)) {
                index = 1;
                beginSort = sortDate;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("XJ");
            sb.append(sortDate);
            sb.append(String.format("%03d", index));
            inspectionVO.setInspectionNumber(sb.toString());

            index ++;
            inspectionVOList.add(inspectionVO);
        }

        return new Result<>(inspectionVOList);
    }

    /**
     * 查询巡检记录
     * @param queryInspectionRequest
     * @return
     * @throws RuntimeException
     */
    @Override
    public PageQuery getInspectionPage(QueryInspectionRequest queryInspectionRequest) throws RuntimeException {

        Integer startNo = PageQuery.startLine(queryInspectionRequest.getCurrPage(), queryInspectionRequest.getPageSize());
        Integer endNo = PageQuery.endLine(queryInspectionRequest.getCurrPage(), queryInspectionRequest.getPageSize());

        Integer progress = null;
        Integer type = null;

        if (queryInspectionRequest.getProgress() != null) {
            progress = queryInspectionRequest.getProgress().getCode();
        }
        if (queryInspectionRequest.getType() != null) {
            type = queryInspectionRequest.getType().getCode();
        }

        Integer total = projectDao.getInspectionCount(progress, queryInspectionRequest.getCreateTime(), type);

        List<InspectionRecord> inspectionRecords = projectDao.getInspectionList(progress, queryInspectionRequest.getCreateTime(), type, startNo, endNo);

        // 如果没有查到，直接返回空
        if (CollectionUtils.isEmpty(inspectionRecords)) {
            PageQuery pageQuery = new PageQuery(queryInspectionRequest.getCurrPage(), queryInspectionRequest.getPageSize(),
                    total, Collections.emptyList());
            return pageQuery;
        }

        List<InspectionVO> inspectionVOList = new ArrayList<>();

        // 先查询一下第一条记录的Sort值，比如210825165314，这个时候要给它编号的话，必须知道同一天该条记录前面还有多少条记录
        String endSort = inspectionRecords.get(0).getSort();
        String beginSort = endSort.substring(0, 6) + "000000";
        int index = projectDao.findCountBySort(beginSort, endSort) + 1;

        beginSort = beginSort.substring(0, 6);
        for (InspectionRecord inspectionRecord : inspectionRecords) {
            InspectionVO inspectionVO = new InspectionVO();

            inspectionVO.setAddress(inspectionRecord.getAddress());
            inspectionVO.setCreateTime(inspectionRecord.getCreateTime());
            inspectionVO.setDescription(inspectionRecord.getDescription());
            inspectionVO.setInspectionId(inspectionRecord.getInspectionId());
            inspectionVO.setInspector(inspectionRecord.getInspector());
            inspectionVO.setPhone(inspectionRecord.getPhone());
            inspectionVO.setProgress(ProgressType.getName(inspectionRecord.getProgress()));
            inspectionVO.setProjectName(inspectionRecord.getProjectName());
            inspectionVO.setType(InspectionType.getName(inspectionRecord.getType()));
            inspectionVO.setRectifyComment(inspectionRecord.getRectifyComment());

            String sortDate = inspectionRecord.getSort().substring(0, 6);

            if (!beginSort.equals(sortDate)) {
                index = 1;
                beginSort = sortDate;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("XJ");
            sb.append(sortDate);
            sb.append(String.format("%03d", index));
            inspectionVO.setInspectionNumber(sb.toString());

            index ++;
            inspectionVOList.add(inspectionVO);
        }

        PageQuery pageQuery = new PageQuery(queryInspectionRequest.getCurrPage(), queryInspectionRequest.getPageSize(),
                total, inspectionVOList);

        return pageQuery;
    }

    /**
     * 获取巡检详情
     * @param inspectionVO
     * @return
     * @throws RuntimeException
     */
    @Override
    public Result getInspectionInfo(InspectionVO inspectionVO) throws RuntimeException {

        // 获取巡检照片
        List<String> fileNames = projectDao.getFiles(UploadFileType.INSPECTION.getCode(), inspectionVO.getInspectionId());
        inspectionVO.setFileNames(fileNames);

        // 获取整改记录
        List<RectificationRecord> rectificationRecords = projectDao.getRectificationList(inspectionVO.getInspectionId());

        List<RectificationVO> rectificationVOS = new ArrayList<>();

        if (!CollectionUtils.isEmpty(rectificationRecords)) {

            String beginSort = rectificationRecords.get(0).getSort().substring(0, 6);
            Integer index = 1;

            for (RectificationRecord rectificationRecord : rectificationRecords) {
                RectificationVO rectificationVO = new RectificationVO();

                rectificationVO.setCreateTime(rectificationRecord.getCreateTime());
                rectificationVO.setDescription(rectificationRecord.getDescription());
                rectificationVO.setRectificationId(rectificationRecord.getRectificationId());
                rectificationVO.setInspectionId(rectificationRecord.getInspectionId());
                rectificationVO.setRectifyUser(rectificationRecord.getRectifyUser());

                String sortDate = rectificationRecord.getSort().substring(0, 6);

                if (!beginSort.equals(sortDate)) {
                    index = 1;
                    beginSort = sortDate;
                }

                StringBuilder sb = new StringBuilder();
                sb.append("ZG");
                sb.append(sortDate);
                sb.append(String.format("%03d", index));
                rectificationVO.setRectificationNumber(sb.toString());
                rectificationVOS.add(rectificationVO);
                index ++;
            }
        }

        inspectionVO.setRectificationList(rectificationVOS);
        return new Result<>(inspectionVO);
    }

    /**
     * 获取整改列表
     * @param inspectionId
     * @return
     * @throws RuntimeException
     */
    @Override
    public Result getRectificationList(String inspectionId) throws RuntimeException {
        // 获取整改记录
        List<RectificationRecord> rectificationRecords = projectDao.getRectificationList(inspectionId);

        List<RectificationVO> rectificationVOS = new ArrayList<>();

        if (!CollectionUtils.isEmpty(rectificationRecords)) {

            String beginSort = rectificationRecords.get(0).getSort().substring(0, 6);
            Integer index = 1;

            for (RectificationRecord rectificationRecord : rectificationRecords) {
                RectificationVO rectificationVO = new RectificationVO();

                rectificationVO.setCreateTime(rectificationRecord.getCreateTime());
                rectificationVO.setDescription(rectificationRecord.getDescription());
                rectificationVO.setRectificationId(rectificationRecord.getRectificationId());
                rectificationVO.setInspectionId(rectificationRecord.getInspectionId());
                rectificationVO.setRectifyUser(rectificationRecord.getRectifyUser());

                String sortDate = rectificationRecord.getSort().substring(0, 6);

                if (!beginSort.equals(sortDate)) {
                    index = 1;
                    beginSort = sortDate;
                }

                StringBuilder sb = new StringBuilder();
                sb.append("ZG");
                sb.append(sortDate);
                sb.append(String.format("%03d", index));
                rectificationVO.setRectificationNumber(sb.toString());
                rectificationVOS.add(rectificationVO);
                index ++;
            }
        }

        return new Result<>(rectificationVOS);
    }

    /**
     * 获取整改详情
     * @param rectificationVO
     * @return
     * @throws RuntimeException
     */
    @Override
    public Result getRectificationInfo(RectificationVO rectificationVO) throws RuntimeException {
        // 获取整改照片
        List<String> fileNames = projectDao.getFiles(UploadFileType.RECTIFICATION.getCode(), rectificationVO.getRectificationId());
        rectificationVO.setFileNames(fileNames);

        return new Result<>(rectificationVO);
    }

    /**
     * 删除整改记录
     * @param rectificationId
     * @return
     * @throws RuntimeException
     */
    @Override
    public Result deleteRectification(String rectificationId) throws RuntimeException {

        projectDao.deleteRectification(rectificationId);

        return new Result(true, "删除整改成功！");
    }

    /**
     * 审阅巡检
     * @param reviewInspectionRequest
     * @return
     * @throws RuntimeException
     */
    @Override
    public Result reviewInspection(ReviewInspectionRequest reviewInspectionRequest) throws RuntimeException {

        projectDao.updateInspection(reviewInspectionRequest.getInspectionId(), reviewInspectionRequest.getProgress().getCode(), reviewInspectionRequest.getRectifyComment());

        return new Result(true, "审阅巡检成功!");
    }

    /**
     * 坐标转换
     * @return
     */
    private Map<String, String> coordinateConvert(String x, String y, String type) {

        String functionName = "";
        if (type.equals("PCTOPAD")) {
            functionName = pcToPad;
        } else {
            functionName = padToPc;
        }

        Map<String, String> coordinateMap = new HashMap<>();

        try {
            QName qName = new QName(nameSpace, functionName);
            Object[] objects = client.invoke(qName, Double.valueOf(x), Double.valueOf(y));

            JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(objects[0]));
            jsonObject.getDouble("x");

            coordinateMap.put("x", jsonObject.getDouble("x").toString());
            coordinateMap.put("y", jsonObject.getDouble("y").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return coordinateMap;

    }
}
