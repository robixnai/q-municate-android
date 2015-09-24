package com.quickblox.q_municate.ui.fragments.call;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.quickblox.q_municate.R;
import com.quickblox.q_municate.utils.image.ImageLoaderUtils;
import com.quickblox.q_municate.ui.fragments.base.BaseFragment;
import com.quickblox.q_municate.ui.views.roundedimageview.RoundedImageView;
import com.quickblox.q_municate_core.utils.ConstsCore;
import com.quickblox.q_municate_core.utils.ErrorUtils;
import com.quickblox.q_municate_db.models.User;

public class IncomingCallFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = IncomingCallFragment.class.getSimpleName();
    private com.quickblox.videochat.webrtc.Consts.MEDIA_STREAM callType;

    private IncomingCallClickListener incomingCallClickListener;
    private User friend;

    public interface IncomingCallClickListener {

        public void onAcceptClick();

        public void onDenyClick();
    }

    public static IncomingCallFragment newInstance(com.quickblox.videochat.webrtc.Consts.MEDIA_STREAM callType, User friend) {
        IncomingCallFragment fragment = new IncomingCallFragment();
        Bundle args = new Bundle();
        args.putSerializable(ConstsCore.CALL_TYPE_EXTRA, callType);
        args.putSerializable(ConstsCore.EXTRA_FRIEND, friend);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_popup_call, container, false);
        callType = (com.quickblox.videochat.webrtc.Consts.MEDIA_STREAM) getArguments().getSerializable(
                ConstsCore.CALL_TYPE_EXTRA);
        friend = (User) getArguments().getSerializable(ConstsCore.EXTRA_FRIEND);
        boolean isVideoCall = com.quickblox.videochat.webrtc.Consts.MEDIA_STREAM.VIDEO.equals(callType);
        ((ImageButton) rootView.findViewById(R.id.acceptCallButton)).setImageResource(
                isVideoCall ? R.drawable.ic_video_white : R.drawable.ic_call_white);
        ((TextView) rootView.findViewById(R.id.callTextView)).setText(
                isVideoCall ? R.string.cll_incoming_call_video : R.string.cll_incoming_call_audio);
        ((TextView) rootView.findViewById(R.id.name_textview)).setText(friend.getFullName());
        RoundedImageView avatarView = (RoundedImageView) rootView.findViewById(R.id.avatar_imageview);
        avatarView.setOval(true);
        if(!TextUtils.isEmpty(friend.getAvatar())){
            ImageLoader.getInstance().displayImage(friend.getAvatar(),
                    avatarView, ImageLoaderUtils.UIL_USER_AVATAR_DISPLAY_OPTIONS);
        }
        rootView.findViewById(R.id.acceptCallButton).setOnClickListener(this);
        rootView.findViewById(R.id.denyCallButton).setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            incomingCallClickListener = (IncomingCallClickListener) activity;
        } catch (ClassCastException e) {
            ErrorUtils.logError(TAG, e);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        incomingCallClickListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.acceptCallButton:
                accept();
                break;
            case R.id.denyCallButton:
                reject();
                break;
            default:
                break;
        }
    }

    private void reject() {
        if (incomingCallClickListener != null) {
            incomingCallClickListener.onDenyClick();
        }
    }


    private void accept() {
        if (incomingCallClickListener != null) {
            incomingCallClickListener.onAcceptClick();
        }
    }
}