begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.testsuite
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|testsuite
package|;
end_package

begin_interface
annotation|@
name|FunctionalInterface
DECL|interface|ThrowingConsumer
specifier|public
interface|interface
name|ThrowingConsumer
parameter_list|<
name|T
parameter_list|>
block|{
DECL|method|accept (T t)
name|void
name|accept
parameter_list|(
name|T
name|t
parameter_list|)
throws|throws
name|Exception
function_decl|;
DECL|method|acceptAndThrowSilently (T t)
specifier|default
name|void
name|acceptAndThrowSilently
parameter_list|(
name|T
name|t
parameter_list|)
block|{
try|try
block|{
name|accept
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_interface

end_unit

