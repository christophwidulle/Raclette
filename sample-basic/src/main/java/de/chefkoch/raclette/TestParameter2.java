package de.chefkoch.raclette;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by christophwidulle on 22.04.16.
 */
public class TestParameter2 implements Parcelable {

    String vla;
    int blub;

    protected TestParameter2(Parcel in) {
        vla = in.readString();
        blub = in.readInt();
    }

    public static final Creator<TestParameter2> CREATOR = new Creator<TestParameter2>() {
        @Override
        public TestParameter2 createFromParcel(Parcel in) {
            return new TestParameter2(in);
        }

        @Override
        public TestParameter2[] newArray(int size) {
            return new TestParameter2[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(vla);
        dest.writeInt(blub);
    }
}
