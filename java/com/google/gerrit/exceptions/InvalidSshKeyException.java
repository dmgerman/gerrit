begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.exceptions
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|exceptions
package|;
end_package

begin_comment
comment|/** Error indicating the SSH key string is invalid as supplied. */
end_comment

begin_class
DECL|class|InvalidSshKeyException
specifier|public
class|class
name|InvalidSshKeyException
extends|extends
name|Exception
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|field|MESSAGE
specifier|public
specifier|static
specifier|final
name|String
name|MESSAGE
init|=
literal|"Invalid SSH Key"
decl_stmt|;
DECL|method|InvalidSshKeyException ()
specifier|public
name|InvalidSshKeyException
parameter_list|()
block|{
name|super
argument_list|(
name|MESSAGE
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

