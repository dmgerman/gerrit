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
DECL|package|com.google.codereview.manager.merge
package|package
name|com
operator|.
name|google
operator|.
name|codereview
operator|.
name|manager
operator|.
name|merge
package|;
end_package

begin_comment
comment|/** Indicates the current branch's queue cannot be processed at this time. */
end_comment

begin_class
DECL|class|MergeException
class|class
name|MergeException
extends|extends
name|Exception
block|{
DECL|method|MergeException (final String msg)
name|MergeException
parameter_list|(
specifier|final
name|String
name|msg
parameter_list|)
block|{
name|super
argument_list|(
name|msg
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|MergeException (final String msg, final Throwable why)
name|MergeException
parameter_list|(
specifier|final
name|String
name|msg
parameter_list|,
specifier|final
name|Throwable
name|why
parameter_list|)
block|{
name|super
argument_list|(
name|msg
argument_list|,
name|why
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

