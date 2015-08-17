package com.github.sqlcteator;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.sqlcteator.test.po.WhPickingWall;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

public class Main {

	public static void main(String[] args) {
		try {
			// List<String> readLines = Files.readLines(new File("D:/so.txt"), Charset.defaultCharset());
			// String str =
			// "AO201506023420,AO201506022733,AO201506025101,AO201506023211,AO201506023292,AO201506023561,AO201506023023,AO201506026183,AO201506024032,AO201506022820,AO201506022831,AO201506023325,AO201506022740,AO201506022922,AO201506023142,AO201506023680,AO201506023234,AO201506022842,AO201506022761,AO201506022708,AO201506024647,AO201506022966,AO201506026203,AO201506024582,AO201506025437,AO201506022735,AO201506022992,AO201506022999,AO201506023996,AO201506023493,AO201506023165,AO201506025084,AO201506023309,AO201506023597,AO201506025508,AO201506025441,AO201506022805,AO201506026160,AO201506023483,AO201506025914,AO201506025531,AO201506022757,AO201506022829,AO201506026153,AO201506024222,AO201506022726,AO201506023772,AO201506024019,AO201506024630,AO201506022777,AO201506022751,AO201506024280,AO201506023706,AO201506022815,AO201506023729,AO201506025879,AO201506023192,AO201506024173,AO201506022739,AO201506022693,AO201506022709,AO201506022936,AO201506025554,AO201506025397,AO201506023994,AO201506024527,AO201506023058,AO201506024130,AO201506022833,AO201506025028,AO201506024310,AO201506024164,AO201506022787,AO201506026235,AO201506022858,AO201506023082,AO201506022963,AO201506026313,AO201506022902,AO201506022840,AO201506023457,AO201506022752,AO201506026152";
			// List<String> readLines = Splitter.on(",").splitToList(str);
			// List<List<String>> newReadLines = Lists.newArrayList();
			// int i = 1;
			// List<String> newArrayList = Lists.newArrayList();
			// for (String s : readLines) {
			// newArrayList.add(s);
			// if (i % 100 == 0) {
			// newReadLines.add(newArrayList);
			// newArrayList = Lists.newArrayList();
			// }
			// i++;
			// }
			// if (newArrayList != null && newArrayList.size() > 0) {
			// newReadLines.add(newArrayList);
			// }
			// String join = null;
			// String strAppend = null;
			// for (List<String> strList : newReadLines) {
			// join = Joiner.on(",").join(strList);
			// strAppend = strAppend(join, ",", "'");
			// System.out.println(strAppend);
			// System.out.println("");
			// }
			//
			// name();
			//
			// System.out.println(StringUtils.removeEnd("1,2,3,", ","));
			WhPickingWall whPickingWall = new WhPickingWall();
			WhPickingWall whPickingWall2 = new WhPickingWall();
			List<WhPickingWall> newArrayList = Lists.newArrayList(whPickingWall,whPickingWall2);
			System.out.println(newArrayList);
			newArrayList.remove(whPickingWall2);
			System.out.println(newArrayList);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void name() {
		StringBuilder sql = new StringBuilder();
		sql.append(" select ao.id ,ao.receive_mobile,ao.receive_tel,ao.receive_post_code,");
		sql.append(" ao.receive_province_name,ao.receive_city_name,ao.receive_area_name,ao.receive_address,");
		sql.append(" ao.logistics_coop_id,ao.receive_name,ao.status,");
		sql.append(" ao.biz_form_id,ao.biz_no,ao.biz_form_type,ao.product_qtys,ao.logi_lock_yn,");
		sql.append(
				" ao.receive_card_no,ao.elec_req_num,ao.whf_pos_id, ao.form_no,ao.no_fly_yn,ao.batch_no,ao.source_order_no");
		sql.append(" from biz_allocate_order ao");
		sql.append(" where ao.elec_succ = 'A' and ao.merge_status != 'WhMergeStatus_C' ");
		sql.append(" and ao.in_picking_yn = 'N' ");
		sql.append(" and ao.status in ('FlowInsStatus_1','FlowInsStatus_2') ");
		sql.append(" and exists (select 1 from set_partner_coop coop, set_partner p ");
		sql.append(
				" where coop.partner_id = p.id and ao.logistics_coop_id = coop.id and p.partner_code in ('SF','YTO','STO','ZTO','ZJS')");
		sql.append(" )");
		System.out.println(sql.toString());
	}

	/**
	 * 判断字符串是否为null或空字符串,在模糊查询的时候很有意义
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0 || str.trim().length() == 0;
	}

	/**
	 * 字符串前后拼接给定字符，如：SO1506116511,SO1506117038 以字符"'"拼接结果：'SO1506116511','SO1506117038'
	 * 
	 * @param str
	 *            原字符串
	 * @param splitStr
	 *            原字符串分割字符
	 * @param appendStr
	 *            前后拼接给定字符
	 * @return String
	 * @Author 杨健/YangJian
	 * @Date 2015年6月18日 下午12:00:04
	 * @Version 1.0.0
	 */
	public static String strAppend(String str, String splitStr, String appendStr) {
		if (isEmpty(str)) {
			return null;
		}
		List<String> strList = Splitter.on(splitStr).splitToList(StringUtils.replaceChars(str, appendStr, ""));
		List<String> newStrList = Lists.newArrayList();
		for (String s : strList) {
			if (isEmpty(s)) {
				continue;
			}
			newStrList.add(StringUtils.join(appendStr, s, appendStr));
		}
		return Joiner.on(splitStr).skipNulls().join(newStrList);
	}

}
