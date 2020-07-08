package com.baseframework.config.poi.excel.demo;

/**
 * @author 邵锦杰
 * @time 2019/4/3
 * @description ${description}
 */
public class Test {
//    public static void main(String[] args) throws Exception {
//        System.out.println(Boolean.TRUE.toString().equals("true"));
//    }
//    public static void main(String[] args) throws Exception {

//        ExcelResult result = ExcelReadUtil.readExcelData(new File("C:\\\\Users\\\\Administrator\\\\Desktop\\\\test\\\\read.xls"), User.class, Role.class);
//        //自定义检查数据
//        //获取sheet数据
//        ExcelSheetData<User> excelSheetData = (ExcelSheetData<User>) result.getDataList().get(0);
//        //每一行数据
//        for (ExcelRowData<User> excelRowData : excelSheetData.getRowDataList()) {
//            //每一列数据
//            for (ExcelRowColData excelRowColData : excelRowData.getRowColDataList()) {
//                if (excelRowColData.getFieldName().equals("username")) {
//                    //此处可以对username，也就是学生姓名进行自定义判断，比如是否重名
//                    /*if(有错误){
//                        excelRowColData.getErrMsgList().add("学生姓名已经重名了");
//                    }*/
//                }
//            }
//        }
//
//        //对正确的数据进行持久化或者其他操作
//        //每一行数据
//        for (ExcelRowData<User> excelRowData : excelSheetData.getRowDataList()) {
//            if(excelRowData.success()){
//                //如果此条数据全部正确，进行操作
//                User user = excelRowData.getData();
//            }else{
//                //失败
//            }
//        }
//
//        if (result.success()) {
//            System.out.println("全部导入成功");
//        } else {
//            //生成错误提示文件
//            String filePath = result.markFile();
//            System.out.println("导入有错误，请查看错误文件");
//        }


        //获取用户数据
//        List<User> userList = new ArrayList<User>() {{
////            add(new User("用户1", 12, 1, LocalDate.now(), 175.0, ""));
////            add(new User("用户2", 22, 2, LocalDate.now(), 150.0, ""));
////            add(new User("用户3", 16, 2, LocalDate.now(), 160.0, ""));
//        }};
//
//        for (int i = 0; i < 200; i++) {
//            userList.add(new User("用户" + (i + 1), 12, 1, LocalDate.now(), 175.0, ""));
//        }
//        ExcelWriteData<User> excelWriteData_user = new ExcelWriteData<>(userList, User.class);
//
//        //获取角色数据
//        List<Role> roleList = new ArrayList<Role>() {{
//        }};
//        for (int i = 0; i < 180; i++) {
//            roleList.add(new Role("角色" + (i + 1)));
//        }
//        ExcelWriteData<Role> excelWriteData_role = new ExcelWriteData<>(roleList, Role.class);
//
//        //写入导出文件，返回导出文件地址
//        String filepath = writeExcel(excelWriteData_user, excelWriteData_role);
//    }
}
