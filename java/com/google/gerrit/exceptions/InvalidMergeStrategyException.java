begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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

begin_class
DECL|class|InvalidMergeStrategyException
specifier|public
class|class
name|InvalidMergeStrategyException
extends|extends
name|RuntimeException
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
DECL|method|InvalidMergeStrategyException (String strategy)
specifier|public
name|InvalidMergeStrategyException
parameter_list|(
name|String
name|strategy
parameter_list|)
block|{
name|super
argument_list|(
literal|"invalid merge strategy: "
operator|+
name|strategy
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

