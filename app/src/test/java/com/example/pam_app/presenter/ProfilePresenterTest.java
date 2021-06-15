package com.example.pam_app.presenter;

import com.example.pam_app.view.ProfileView;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ProfilePresenterTest {

    @Mock
    private ProfileView profileView;

    private ProfilePresenter presenter;

    @BeforeEach
    public void setUp() {
        presenter = new ProfilePresenter(profileView);
    }

    @Test
    public void givenClickOnApplyChangesClickedThenApplyChangesInView() {
        presenter.onApplyChangesClicked();
        verify(profileView, only()).applyChanges();
    }
}
