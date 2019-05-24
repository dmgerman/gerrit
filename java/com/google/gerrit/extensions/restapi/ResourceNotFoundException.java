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
DECL|package|com.google.gerrit.extensions.restapi
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|restapi
package|;
end_package

begin_comment
comment|/** Named resource does not exist (HTTP 404 Not Found). */
end_comment

begin_class
DECL|class|ResourceNotFoundException
specifier|public
class|class
name|ResourceNotFoundException
extends|extends
name|RestApiException
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
comment|/** Requested resource is not found, failing portion not specified. */
DECL|method|ResourceNotFoundException ()
specifier|public
name|ResourceNotFoundException
parameter_list|()
block|{}
DECL|method|ResourceNotFoundException (String msg)
specifier|public
name|ResourceNotFoundException
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
DECL|method|ResourceNotFoundException (String msg, Throwable cause)
specifier|public
name|ResourceNotFoundException
parameter_list|(
name|String
name|msg
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|msg
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
comment|/** @param id portion of the resource URI that does not exist. */
DECL|method|ResourceNotFoundException (IdString id)
specifier|public
name|ResourceNotFoundException
parameter_list|(
name|IdString
name|id
parameter_list|)
block|{
name|super
argument_list|(
literal|"Not found: "
operator|+
name|id
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|caching (CacheControl c)
specifier|public
name|ResourceNotFoundException
name|caching
parameter_list|(
name|CacheControl
name|c
parameter_list|)
block|{
name|setCaching
argument_list|(
name|c
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

