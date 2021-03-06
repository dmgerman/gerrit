begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.auth
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|auth
package|;
end_package

begin_comment
comment|/**  * An authentication exception that is thrown when the user credentials are valid, but not allowed  * to authenticate for other reasons i.e. account disabled.  */
end_comment

begin_class
DECL|class|UserNotAllowedException
specifier|public
class|class
name|UserNotAllowedException
extends|extends
name|AuthException
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|1531411999932922558L
decl_stmt|;
DECL|method|UserNotAllowedException ()
specifier|public
name|UserNotAllowedException
parameter_list|()
block|{}
DECL|method|UserNotAllowedException (String msg)
specifier|public
name|UserNotAllowedException
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
name|super
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
DECL|method|UserNotAllowedException (Throwable ex)
specifier|public
name|UserNotAllowedException
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
name|super
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
DECL|method|UserNotAllowedException (String msg, Throwable ex)
specifier|public
name|UserNotAllowedException
parameter_list|(
name|String
name|msg
parameter_list|,
name|Throwable
name|ex
parameter_list|)
block|{
name|super
argument_list|(
name|msg
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

