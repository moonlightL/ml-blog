package com.extlight.service.impl;

import com.extlight.component.CommonMap;
import com.extlight.component.MailService;
import com.extlight.mapper.BaseMapper;
import com.extlight.mapper.GuestbookMapper;
import com.extlight.model.Guestbook;
import com.extlight.service.GuestbookService;
import com.extlight.web.exception.GlobalException;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.util.StringUtil;

import java.util.List;

@Service
public class GuestbookServiceImpl extends BaseServiceImpl<Guestbook> implements GuestbookService {

    @Autowired
    private GuestbookMapper guestbookMapper;
    @Autowired
    private MailService mailService;
    @Autowired
    private CommonMap commonMap;

    @Override
    public BaseMapper<Guestbook> getBaseMapper() {
        return guestbookMapper;
    }

    @Override
    public int getGuestbookCount() {
        return this.guestbookMapper.selectCount(null);
    }

    @Override
    public void deleteGuestbook(Integer id) throws GlobalException{
        Guestbook guestbook = new Guestbook();
        guestbook.setId(id);
        guestbook.setDelStatus(1);
        this.guestbookMapper.updateByPrimaryKeySelective(guestbook);
    }

    @Override
    public List<Guestbook> getListPyPage(Integer delStatus,Integer type, Integer pageNum, Integer pageSize) throws GlobalException{
        if (pageNum < 1) {
            pageNum = 1;
        }
        PageHelper.startPage(pageNum,pageSize);
        return this.guestbookMapper.getList(delStatus,type);
    }

    @Override
    public Integer getTotalCount(int delStatus) {
        return this.guestbookMapper.getTotalCount(delStatus);
    }

    @Override
    public void reply(Guestbook guestbook) throws GlobalException {
        guestbook.setNickname(commonMap.get("author").toString());
        guestbook.setEmail(commonMap.get("email").toString());
        guestbook.setStatus(0);
        guestbook.setDelStatus(0);
        guestbook.setType(2);
        this.guestbookMapper.insert(guestbook);

        // 发送邮件
        Guestbook gb = this.guestbookMapper.selectByPrimaryKey(guestbook.getGuestbookId());
        if (gb != null && !StringUtil.isEmpty(gb.getEmail())) {
            try {
                this.mailService.sendEmail(gb.getEmail(),guestbook.getNickname() + "的留言回复【"+commonMap.get("blogName").toString()+"】",guestbook.getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void save(Guestbook guestbook) throws GlobalException {

        Guestbook tmp = this.guestbookMapper.getByNickname(guestbook.getNickname());
        if (tmp != null) {
            throw new GlobalException(400,"昵称重复,请重新换一个昵称");
        }

        guestbook.setStatus(0);
        guestbook.setDelStatus(0);
        guestbook.setType(guestbook.getGuestbookId() == null ? 1 : 2);
        this.guestbookMapper.insert(guestbook);

        // 发送邮件给博主
        try {
            if (!StringUtils.isEmpty(this.commonMap.get("email"))) {
                this.mailService.sendEmail(this.commonMap.get("email").toString(),guestbook.getNickname() + "的留言",guestbook.getContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (guestbook.getGuestbookId() != null) {
            // 发送邮件给被回复者
            Guestbook gb = this.guestbookMapper.selectByPrimaryKey(guestbook.getGuestbookId());
            if (gb != null && !StringUtil.isEmpty(gb.getEmail())) {
                try {
                    this.mailService.sendEmail(gb.getEmail(),guestbook.getNickname() + "的留言回复【"+commonMap.get("blogName").toString()+"】",guestbook.getContent());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
