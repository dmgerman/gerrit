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
DECL|package|com.google.gerrit.index.query
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|index
operator|.
name|query
package|;
end_package

begin_class
DECL|class|TooManyTermsInQueryException
specifier|public
class|class
name|TooManyTermsInQueryException
extends|extends
name|QueryParseException
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
specifier|private
specifier|static
specifier|final
name|String
name|MESSAGE
init|=
literal|"too many terms in query"
decl_stmt|;
DECL|method|TooManyTermsInQueryException ()
specifier|public
name|TooManyTermsInQueryException
parameter_list|()
block|{
name|super
argument_list|(
name|MESSAGE
argument_list|)
expr_stmt|;
block|}
DECL|method|TooManyTermsInQueryException (Throwable why)
specifier|public
name|TooManyTermsInQueryException
parameter_list|(
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

