/* www.Zero1.Sg 2022Dec
https://github.com/alvinzero1/verify/tree/api */
package com.zero1.chkfileid;

import java.util.ArrayList;
import java.util.Map;

public interface Paths {

    public boolean addPrimarypathFilenameToHashmap();

    public boolean targetFilesVerifyByHash();

    public void setHashmap(Map<String, ArrayList<String>> map);
}
