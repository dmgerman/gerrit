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
DECL|package|com.google.gerrit.client.rpc
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|rpc
package|;
end_package

begin_comment
comment|/** Error indicating the server cannot store contact information. */
end_comment

begin_class
DECL|class|ContactInformationStoreException
specifier|public
class|class
name|ContactInformationStoreException
extends|extends
name|Exception
block|{
DECL|field|MESSAGE
specifier|public
specifier|static
specifier|final
name|String
name|MESSAGE
init|=
literal|"Cannot store contact information"
decl_stmt|;
DECL|method|ContactInformationStoreException ()
specifier|public
name|ContactInformationStoreException
parameter_list|()
block|{
name|super
argument_list|(
name|MESSAGE
argument_list|)
expr_stmt|;
block|}
DECL|method|ContactInformationStoreException (final Throwable why)
specifier|public
name|ContactInformationStoreException
parameter_list|(
specifier|final
name|Throwable
name|why
parameter_list|)
block|{
name|super
argument_list|(
name|MESSAGE
argument_list|,
name|why
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

