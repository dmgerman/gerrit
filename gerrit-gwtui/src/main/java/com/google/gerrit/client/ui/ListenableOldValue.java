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
DECL|package|com.google.gerrit.client.ui
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|ui
package|;
end_package

begin_class
DECL|class|ListenableOldValue
specifier|public
class|class
name|ListenableOldValue
parameter_list|<
name|T
parameter_list|>
extends|extends
name|ListenableValue
argument_list|<
name|T
argument_list|>
block|{
DECL|field|oldValue
specifier|private
name|T
name|oldValue
decl_stmt|;
DECL|method|getOld ()
specifier|public
name|T
name|getOld
parameter_list|()
block|{
return|return
name|oldValue
return|;
block|}
DECL|method|set (final T value)
specifier|public
name|void
name|set
parameter_list|(
specifier|final
name|T
name|value
parameter_list|)
block|{
name|oldValue
operator|=
name|get
argument_list|()
expr_stmt|;
name|super
operator|.
name|set
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|oldValue
operator|=
literal|null
expr_stmt|;
comment|// allow it to be gced before the next event
block|}
block|}
end_class

end_unit

