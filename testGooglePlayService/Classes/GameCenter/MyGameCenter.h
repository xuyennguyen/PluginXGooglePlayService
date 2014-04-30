/*
 * MyMyGameCenter.h
 *
 *  Created on: Feb 13, 2014
 *      Author: Xuyen
 */

#ifndef MYGAMECENTER_H_
#define MYGAMECENTER_H_
#include "cocos2d.h"
#if CC_PLATFORM_ANDROID == CC_TARGET_PLATFORM
#include "ProtocolSocial.h"


using namespace cocos2d;
class GooglePLaySharedResult : public cocos2d::plugin::ShareResultListener
{
public:
	void setListener(ShareResultListener *listener);
	virtual void onShareResult(cocos2d::plugin::ResultCode ret, const char* msg);
	virtual void onLoginResult(cocos2d::plugin::ResultCode ret, const char* msg);
	virtual void onScoreResult(cocos2d::plugin::ResultCode ret, const char* msg);

private:
	ShareResultListener *m_pLister;
};

class MyGameCenter
{
public:
	static MyGameCenter* sharedSocialManager();
    static void purgeManager();
	void shareByMode(cocos2d::plugin::TShareInfo info);
	void unloadSocialPlugin();
	void loadSocialPlugin();
	void setListener(cocos2d::plugin::ShareResultListener  *listener);

private:
    MyGameCenter();
    virtual ~MyGameCenter();

    static MyGameCenter* s_pManager;

    cocos2d::plugin::ProtocolSocial* s_pGameCenter;
    GooglePLaySharedResult* s_pRetListener;

};

#endif /* MYMyGameCenter_H_ */
#endif
