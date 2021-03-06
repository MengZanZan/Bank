package com.bankofshanghai.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bankofshanghai.mypojo.BankResult;
import com.bankofshanghai.mypojo.MyDataList;
import com.bankofshanghai.mypojo.MyPageList;
import com.bankofshanghai.pojo.BankData;
import com.bankofshanghai.pojo.BankUser;
import com.bankofshanghai.service.CheckService;
import com.bankofshanghai.service.DataService;
import com.bankofshanghai.service.UsermanService;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/ajax")
public class CheckController {

	@Autowired
	private CheckService checkService;

	@Autowired
	private DataService dataService;

	@Autowired
	private UsermanService usermanService;

	@RequestMapping(value = "/check", method = RequestMethod.GET)
	@ResponseBody
	public BankResult check(@RequestParam(required = false, defaultValue = "10") int pageSize,
			@RequestParam(required = false, defaultValue = "1") int page) {
		// long startTime = System.currentTimeMillis();
		List<BankData> list = dataService.queryByPage(1, 10);
		for (BankData data : list) {
			int r = checkService.check(data);
			System.out.println(r);
		}
		return BankResult.ok(list);
	}

	@RequestMapping(value = "/check/importdata")
	@ResponseBody
	public BankResult check(String filename) {
		List<BankData> list = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			// 读文件
			File file = ResourceUtils.getFile("classpath:sql/file.txt");
			// File file = new File(filename);
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = "";
			while ((line = br.readLine()) != null) {
				String str[] = line.split("\\s+");
				BankData data = new BankData();
				data.setFromuser(Long.valueOf(str[0]));
				data.setTouser(Long.valueOf(str[1]));
				data.setMoney(BigDecimal.valueOf(Double.valueOf(str[2])));
				Date date = sdf.parse(str[3] + " " + str[4]);
				data.setDatetime(date);
				data.setFromplace(str[5]);
				data.setToplace(str[6]);
				data.setTool(str[7]);
				list.add(data);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			return BankResult.build(1, e.getMessage());
		}
		for (BankData data : list) {
			checkService.check(data);
		}
		return BankResult.ok(list);
	}

	// 显示data
	@RequestMapping(value = "/checkdata", method = RequestMethod.GET)
	@ResponseBody
	public BankResult checkData(HttpServletRequest request,
			@RequestParam(required = false, defaultValue = "10") int pageSize,
			@RequestParam(required = false, defaultValue = "1") int page) throws Exception {
		int pageNo = page;
		List<BankData> datalist = dataService.queryByPage(pageNo, pageSize);
		List<MyDataList> datalist2 = dataService.showdata(datalist);
		PageInfo<BankData> pageInfo = new PageInfo<BankData>(datalist);
		MyPageList<MyDataList> list = new MyPageList<>();
		list.setList(datalist2);
		list.setTotal(pageInfo.getTotal());
		return BankResult.ok(list);
	}

	@RequestMapping(value = "/check_imm", method = RequestMethod.POST)
	@ResponseBody
	public BankResult check_test(HttpServletRequest request,
			@RequestParam(required = false, defaultValue = "10") int pageSize,
			@RequestParam(required = false, defaultValue = "1") int page) {

		Long fromuser = null;
		Long touser = null;
		String tool = null;
		Integer moneyint = 0;
		String fromplace = null;
		Integer safety = null;
		Date date_s = null;
		Date date_e = null;
		Integer safe_action = 0;
		List<BankData> datalist = dataService.queryByPage(fromuser, touser, moneyint, fromplace, tool, safety, date_s,
				date_e, safe_action, page, pageSize);

		int n = datalist.size();
		for (int i = 0; i < n; i++) {
			BankData data = datalist.get(i);
			Long userid = (long) data.getFromuser();
			BankUser user = usermanService.getUserByID(userid);
			Integer usertype = user.getUsertype();
			if (usertype == 0)// 黑名单
			{
				data.setSafeLevel(99);
				dataService.updateDataSafe(data);
			} else {

				if (usertype == 1)// 白名单
				{
					data.setSafeLevel(0);
					dataService.updateDataSafe(data);
				}

				else // 灰名单，高风险ip、手机号等
				{
					int result = checkService.check(data);
					if (result == -1) // 检测失败
						return BankResult.build(1, "检测失败");
					else {// 成功
						data.setSafeLevel(result);
					}
				}

			}
		}
		datalist = dataService.queryByPage(fromuser, touser, moneyint, fromplace, tool, safety, date_s, date_e,
				safe_action, page, pageSize);
		List<MyDataList> datalist2 = dataService.showdata(datalist);
		PageInfo<BankData> pageInfo = new PageInfo<BankData>(datalist);
		MyPageList<MyDataList> list = new MyPageList<>();
		list.setList(datalist2);
		list.setTotal(pageInfo.getTotal());
		return BankResult.ok(list);
	}
}
