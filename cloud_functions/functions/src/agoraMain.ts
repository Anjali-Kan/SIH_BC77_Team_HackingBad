import {RtcTokenBuilder,Role} from "./agora/RtcTokenBuilder"

const appId = "a8413a1d0ade491eb6dbdec356e3df05";
const appCertificate = "9d9296d259264bbfa751057565962af3";
const expirationTimeInSeconds = 60*60*24;

let tokenBuilder = (emergencyId:String)=>{
    const currentTimestamp = Math.floor(Date.now() / 1000);
    const privilegeExpiredTs = currentTimestamp + expirationTimeInSeconds;
    return RtcTokenBuilder.buildTokenWithUid(appId,appCertificate,emergencyId,0,Role.PUBLISHER,privilegeExpiredTs);
};

export {tokenBuilder}

