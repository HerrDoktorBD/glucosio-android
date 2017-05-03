package org.glucosio.android.presenter;

import org.glucosio.android.activity.A1cCalculatorActivity;
import org.glucosio.android.db.DatabaseHandler;
import org.glucosio.android.db.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class A1CCalculatorPresenterTest {
    @Mock
    private DatabaseHandler dbHandlerMock;

    @Mock
    private A1cCalculatorActivity activity;

    @InjectMocks
    private A1CCalculatorPresenter presenter;

    @Mock
    private User userMock;

    @Before
    public void setUp() throws Exception {
        when(dbHandlerMock.getUser(anyLong())).thenReturn(userMock);
        when(userMock.getPreferred_unit()).thenReturn("mg/dL");
        when(userMock.getPreferred_unit_a1c()).thenReturn("percentage");
    }

    @Test
    public void shouldReturnZeroWhenNullStringPassed() throws Exception {
        assertThat(presenter.calculateA1C(null)).isZero();
    }

    @Test
    public void shouldReturnZeroWhenEmptyStringPassed() throws Exception {
        assertThat(presenter.calculateA1C(null)).isZero();
    }

    @Test
    public void shouldReturnZeroWhenDecimalSeparatorStringPassed() throws Exception {
        assertThat(presenter.calculateA1C(".")).isZero();
    }

    @Test
    public void shouldReturnZeroWhenAnotherDecimalSeparatorStringPassed() throws Exception {
        assertThat(presenter.calculateA1C(",")).isZero();
    }

    @Test
    public void shouldCalculatableWhenUserPreferredMmol() throws Exception {
        when(userMock.getPreferred_unit()).thenReturn("mmol/L");
        assertThat(presenter.calculateA1C("1")).isEqualTo(2.25);
    }

    @Test
    public void shouldCalculatableWhenUserPreferredPercentage() throws Exception {
        when(userMock.getPreferred_unit_a1c()).thenReturn("mmol/mol");
        assertThat(presenter.calculateA1C("20")).isEqualTo(1.84);
    }

    @Test
    public void shouldCallSetMmolWhenUserPreferredUnitIsNotMgDl() throws Exception {
        when(userMock.getPreferred_unit()).thenReturn("mmol/L");
        presenter.checkGlucoseUnit();
        verify(activity).setMmol();
    }

    @Test
    public void shouldReturnCorrectA1CUnitWhenGetterCalled() throws Exception {
        when(userMock.getPreferred_unit_a1c()).thenReturn("mmol/mol");
        assertThat(presenter.getA1cUnit()).isEqualTo("mmol/mol");
    }

    @Test
    public void shouldFinishActivityAfterSavingWhensaveA1CCalled() throws Exception {
        when(userMock.getPreferred_unit_a1c()).thenReturn("mmol/mol");
        presenter.saveA1C(presenter.calculateA1C("20"));
        verify(activity).finish();
    }
}
