begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|// you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|// You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|// distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|// See the License for the specific language governing permissions and
end_comment

begin_comment
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.testutil
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|testutil
package|;
end_package

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|EasyMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|IMocksControl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|powermock
operator|.
name|api
operator|.
name|easymock
operator|.
name|PowerMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|powermock
operator|.
name|modules
operator|.
name|junit4
operator|.
name|PowerMockRunner
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_comment
comment|/**  * Test case with some support for automatically verifying mocks.  *  * This test case works transparently with EasyMock and PowerMock.  */
end_comment

begin_class
DECL|class|MockingTestCase
specifier|public
specifier|abstract
class|class
name|MockingTestCase
extends|extends
name|TestCase
block|{
DECL|field|mocks
specifier|private
name|Collection
argument_list|<
name|Object
argument_list|>
name|mocks
decl_stmt|;
DECL|field|mockControls
specifier|private
name|Collection
argument_list|<
name|IMocksControl
argument_list|>
name|mockControls
decl_stmt|;
DECL|field|mocksReplayed
specifier|private
name|boolean
name|mocksReplayed
decl_stmt|;
DECL|field|usePowerMock
specifier|private
name|boolean
name|usePowerMock
decl_stmt|;
comment|/**    * Create and register a mock control.    *    * @return The mock control instance.    */
DECL|method|createMockControl ()
specifier|protected
specifier|final
name|IMocksControl
name|createMockControl
parameter_list|()
block|{
name|IMocksControl
name|mockControl
init|=
name|EasyMock
operator|.
name|createControl
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Adding mock control failed"
argument_list|,
name|mockControls
operator|.
name|add
argument_list|(
name|mockControl
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|mockControl
return|;
block|}
comment|/**    * Create and register a mock.    *    * Creates a mock and registers it in the list of created mocks, so it gets    * treated automatically upon {@code replay} and {@code verify};    * @param toMock The class to create a mock for.    * @return The mock instance.    */
DECL|method|createMock (Class<T> toMock)
specifier|protected
specifier|final
parameter_list|<
name|T
parameter_list|>
name|T
name|createMock
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|toMock
parameter_list|)
block|{
return|return
name|createMock
argument_list|(
name|toMock
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**    * Create a mock for a mock control and register a mock.    *    * Creates a mock and registers it in the list of created mocks, so it gets    * treated automatically upon {@code replay} and {@code verify};    * @param toMock The class to create a mock for.    * @param control The mock control to create the mock on. If null, do not use    *    a specific control.    * @return The mock instance.    */
DECL|method|createMock (Class<T> toMock, IMocksControl control)
specifier|protected
specifier|final
parameter_list|<
name|T
parameter_list|>
name|T
name|createMock
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|toMock
parameter_list|,
name|IMocksControl
name|control
parameter_list|)
block|{
name|assertFalse
argument_list|(
literal|"Mocks have already been set to replay"
argument_list|,
name|mocksReplayed
argument_list|)
expr_stmt|;
specifier|final
name|T
name|mock
decl_stmt|;
if|if
condition|(
name|control
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|usePowerMock
condition|)
block|{
name|mock
operator|=
name|PowerMock
operator|.
name|createMock
argument_list|(
name|toMock
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|mock
operator|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|toMock
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
literal|"Adding "
operator|+
name|toMock
operator|.
name|getName
argument_list|()
operator|+
literal|" mock failed"
argument_list|,
name|mocks
operator|.
name|add
argument_list|(
name|mock
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|mock
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|toMock
argument_list|)
expr_stmt|;
block|}
return|return
name|mock
return|;
block|}
comment|/**    * Set all registered mocks to replay    */
DECL|method|replayMocks ()
specifier|protected
specifier|final
name|void
name|replayMocks
parameter_list|()
block|{
name|assertFalse
argument_list|(
literal|"Mocks have already been set to replay"
argument_list|,
name|mocksReplayed
argument_list|)
expr_stmt|;
if|if
condition|(
name|usePowerMock
condition|)
block|{
name|PowerMock
operator|.
name|replayAll
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|EasyMock
operator|.
name|replay
argument_list|(
name|mocks
operator|.
name|toArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|IMocksControl
name|mockControl
range|:
name|mockControls
control|)
block|{
name|mockControl
operator|.
name|replay
argument_list|()
expr_stmt|;
block|}
name|mocksReplayed
operator|=
literal|true
expr_stmt|;
block|}
comment|/**    * Verify all registered mocks    *    * This method is called automatically at the end of a test. Nevertheless,    * it is safe to also call it beforehand, if this better meets the    * verification part of a test.    */
comment|// As the PowerMock runner does not pass through runTest, we inject mock
comment|// verification through @After
annotation|@
name|After
DECL|method|verifyMocks ()
specifier|public
specifier|final
name|void
name|verifyMocks
parameter_list|()
block|{
if|if
condition|(
operator|!
name|mocks
operator|.
name|isEmpty
argument_list|()
operator|||
operator|!
name|mockControls
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|assertTrue
argument_list|(
literal|"Created mocks have not been set to replay. Call replayMocks "
operator|+
literal|"within the test"
argument_list|,
name|mocksReplayed
argument_list|)
expr_stmt|;
if|if
condition|(
name|usePowerMock
condition|)
block|{
name|PowerMock
operator|.
name|verifyAll
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|EasyMock
operator|.
name|verify
argument_list|(
name|mocks
operator|.
name|toArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|IMocksControl
name|mockControl
range|:
name|mockControls
control|)
block|{
name|mockControl
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|usePowerMock
operator|=
literal|false
expr_stmt|;
name|RunWith
name|runWith
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|RunWith
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|runWith
operator|!=
literal|null
condition|)
block|{
name|usePowerMock
operator|=
name|PowerMockRunner
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|runWith
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|mocks
operator|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
expr_stmt|;
name|mockControls
operator|=
operator|new
name|ArrayList
argument_list|<
name|IMocksControl
argument_list|>
argument_list|()
expr_stmt|;
name|mocksReplayed
operator|=
literal|false
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|runTest ()
specifier|protected
name|void
name|runTest
parameter_list|()
throws|throws
name|Throwable
block|{
name|super
operator|.
name|runTest
argument_list|()
expr_stmt|;
comment|// Plain JUnit runner does not pick up @After, so we add it here
comment|// explicitly. Note, that we cannot put this into tearDown, as failure
comment|// to verify mocks would bail out and might leave open resources from
comment|// subclasses open.
name|verifyMocks
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

