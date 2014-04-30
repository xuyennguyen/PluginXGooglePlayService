
#include "MyGameCenter.h"
#include "ProtocolSocial.h"
#include "PluginManager.h"
#include "cocos2d.h"

using namespace cocos2d::plugin;
using namespace cocos2d;

MyGameCenter* MyGameCenter::s_pManager = NULL;
MyGameCenter::MyGameCenter()
: s_pRetListener(NULL)
, s_pGameCenter(NULL)
{

}

MyGameCenter::~MyGameCenter()
{
	unloadSocialPlugin();
	if (s_pRetListener)
	{
		delete s_pRetListener;
		s_pRetListener = NULL;
	}
}

MyGameCenter* MyGameCenter::sharedSocialManager()
{
	if (s_pManager == NULL) {
		s_pManager = new MyGameCenter();
	}
	return s_pManager;
}

void MyGameCenter::purgeManager()
{
	if (s_pManager)
	{
		delete s_pManager;
		s_pManager = NULL;
	}
	PluginManager::end();
}

void MyGameCenter::loadSocialPlugin()
{
	CCLog("goi den ham loadsocialPlugin");
	if (s_pRetListener == NULL)
	{
		s_pRetListener = new GooglePLaySharedResult();
	}
	CCLog("goi den ham loadsocialPlugin 2");

		// init Facebook plugin
	s_pGameCenter= dynamic_cast<ProtocolSocial*>(PluginManager::getInstance()->loadPlugin("PluginGameCenter"));
	CCLog("goi vao ham khoi tao gamecenter");
	
		if (NULL != s_pGameCenter)
		{
			CCLog("goi vao ham khoi tao gamecenter FFF");
			cocos2d::plugin::TSocialDeveloperInfo pGameCenterInfo;
			pGameCenterInfo["name"] = "xuyen";

			if (pGameCenterInfo.empty())
			{
				char msg[256] = { 0 };
				CCLog("abc");
		
			}
			CCLog("goi vao ham khoi tao gamecenter FFF 1");
			s_pGameCenter->setDebugMode(true);

			CCLog("goi vao ham khoi tao gamecenter FFF 2");

			s_pGameCenter->configDeveloperInfo(pGameCenterInfo);
			CCLog("goi vao ham khoi tao gamecenter FFF 3");
			s_pGameCenter->setResultListener(s_pRetListener);
		}
}

void MyGameCenter::unloadSocialPlugin()
{
	if (s_pGameCenter)
	{
		PluginManager::getInstance()->unloadPlugin("PluginGameCenter");
		s_pGameCenter = NULL;
	}
}

void MyGameCenter::shareByMode(TShareInfo info)
{
	if (s_pRetListener == NULL)
	{
	s_pRetListener = new GooglePLaySharedResult();
	}

	info["Name"] = "abc";
	ProtocolSocial* pShare = NULL;
	pShare = s_pGameCenter;
	if (pShare != NULL) {
		CCLog("goi vao ham nay share by mode ");
		pShare->share(info);
		//pShare->setResultListener(s_pRetListener);
	}

}
void GooglePLaySharedResult::onShareResult(ShareResultCode ret, const char* msg)
{
	CCLog("Goi den onShareResult");
	char shareStatus[1024] = { 0 };
	sprintf(shareStatus, "Share %s", (ret == kShareSuccess)? "Successed" : "Failed");
	int value =  0;
    if ( ret ==  kShareSuccess){
    	value = 0 ; // thanh cong
    }
    else if ( ret == kShareFail){
    	value = 1;
    }
    else if ( ret == kShareCancel){
    	value = 2;
    }
    else if ( ret== kShareTimeOut ){
    	value = 3;
    }
    CCUserDefault::sharedUserDefault()->setIntegerForKey("mCode",value);
    CCUserDefault::sharedUserDefault()->flush();

    m_pLister->onShareResult(ret, msg );
}
void MyGameCenter::setListener(ShareResultListener *listener){
	this->s_pRetListener->setListener(listener);
}
void GooglePLaySharedResult::setListener( ShareResultListener *listener){
	m_pLister = listener;
}
void GooglePLaySharedResult::onLoginResult(ShareResultCode code, const char* msg){
}
void GooglePLaySharedResult::onScoreResult(ShareResultCode code, const char* msg){
}
