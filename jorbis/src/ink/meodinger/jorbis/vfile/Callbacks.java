package ink.meodinger.jorbis.vfile;

/**
 * Author: Meodinger
 * Date: 2022/1/16
 * Location: ink.meodinger.jorbis.vfile.internal
 */
public abstract class Callbacks {

    public abstract long read(Object pointer, long size, long nmemb, Object dataSource);

    public abstract int seek(Object dataSource, long offset, int whence);

    public abstract int close(Object dataSource);

    public abstract int tell(Object dataSource);

}
