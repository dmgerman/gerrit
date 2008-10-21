begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
DECL|package|com.google.codereview.rpc
package|package
name|com
operator|.
name|google
operator|.
name|codereview
operator|.
name|rpc
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|protobuf
operator|.
name|RpcCallback
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_class
DECL|class|SimpleControllerTest
specifier|public
class|class
name|SimpleControllerTest
extends|extends
name|TestCase
block|{
DECL|method|testDefaultConstructor ()
specifier|public
name|void
name|testDefaultConstructor
parameter_list|()
block|{
specifier|final
name|SimpleController
name|sc
init|=
operator|new
name|SimpleController
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|sc
operator|.
name|errorText
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|sc
operator|.
name|failed
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|sc
operator|.
name|isCanceled
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testNotifyCancelUnsupported ()
specifier|public
name|void
name|testNotifyCancelUnsupported
parameter_list|()
block|{
try|try
block|{
operator|new
name|SimpleController
argument_list|()
operator|.
name|notifyOnCancel
argument_list|(
operator|new
name|RpcCallback
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|(
name|Object
name|parameter
parameter_list|)
block|{
name|fail
argument_list|(
literal|"Callback invoked during notifyOnCancel setup"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"notifyOnCancel accepted a callback"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
comment|// Pass
block|}
block|}
DECL|method|testStartCancelUnsupported ()
specifier|public
name|void
name|testStartCancelUnsupported
parameter_list|()
block|{
try|try
block|{
operator|new
name|SimpleController
argument_list|()
operator|.
name|startCancel
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"startCancel did not fail"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
comment|// Pass
block|}
block|}
DECL|method|testSetFailed ()
specifier|public
name|void
name|testSetFailed
parameter_list|()
block|{
specifier|final
name|String
name|reason
init|=
literal|"we failed, yes we did"
decl_stmt|;
specifier|final
name|SimpleController
name|sc
init|=
operator|new
name|SimpleController
argument_list|()
decl_stmt|;
name|sc
operator|.
name|setFailed
argument_list|(
name|reason
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sc
operator|.
name|failed
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|reason
argument_list|,
name|sc
operator|.
name|errorText
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testResetClearedFailure ()
specifier|public
name|void
name|testResetClearedFailure
parameter_list|()
block|{
specifier|final
name|String
name|reason
init|=
literal|"we failed, yes we did"
decl_stmt|;
specifier|final
name|SimpleController
name|sc
init|=
operator|new
name|SimpleController
argument_list|()
decl_stmt|;
name|sc
operator|.
name|setFailed
argument_list|(
name|reason
argument_list|)
expr_stmt|;
name|sc
operator|.
name|reset
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
name|sc
operator|.
name|errorText
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|sc
operator|.
name|failed
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|sc
operator|.
name|isCanceled
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

