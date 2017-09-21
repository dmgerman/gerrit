begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.api
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|api
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Throwables
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|restapi
operator|.
name|RestApiException
import|;
end_import

begin_comment
comment|/** Static utilities for API implementations. */
end_comment

begin_class
DECL|class|ApiUtil
specifier|public
class|class
name|ApiUtil
block|{
comment|/**    * Convert an exception encountered during API execution to a {@link RestApiException}.    *    * @param msg message to be used in the case where a new {@code RestApiException} is wrapped    *     around {@code e}.    * @param e exception being handled.    * @return {@code e} if it is already a {@code RestApiException}, otherwise a new {@code    *     RestApiException} wrapped around {@code e}.    * @throws RuntimeException if {@code e} is a runtime exception, it is rethrown as-is.    */
DECL|method|asRestApiException (String msg, Exception e)
specifier|public
specifier|static
name|RestApiException
name|asRestApiException
parameter_list|(
name|String
name|msg
parameter_list|,
name|Exception
name|e
parameter_list|)
throws|throws
name|RuntimeException
block|{
name|Throwables
operator|.
name|throwIfUnchecked
argument_list|(
name|e
argument_list|)
expr_stmt|;
return|return
name|e
operator|instanceof
name|RestApiException
condition|?
operator|(
name|RestApiException
operator|)
name|e
else|:
operator|new
name|RestApiException
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
return|;
block|}
DECL|method|ApiUtil ()
specifier|private
name|ApiUtil
parameter_list|()
block|{}
block|}
end_class

end_unit

