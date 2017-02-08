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
DECL|package|com.google.gerrit.server.notedb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|notedb
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|time
operator|.
name|format
operator|.
name|DateTimeFormatter
operator|.
name|ISO_INSTANT
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|TypeAdapter
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|stream
operator|.
name|JsonReader
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|stream
operator|.
name|JsonWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Instant
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|LocalDateTime
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|ZoneId
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|format
operator|.
name|DateTimeFormatter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|format
operator|.
name|DateTimeParseException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|format
operator|.
name|FormatStyle
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|temporal
operator|.
name|TemporalAccessor
import|;
end_import

begin_comment
comment|/**  * Adapter that reads/writes {@link Timestamp}s as ISO 8601 instant in UTC.  *  *<p>This adapter reads and writes the ISO 8601 UTC instant format, {@code "2015-06-22T17:11:00Z"}.  * This format is specially chosen because it is also readable by the default Gson type adapter,  * despite the fact that the default adapter writes in a different format lacking timezones, {@code  * "Jun 22, 2015 10:11:00 AM"}. Unlike the default adapter format, this representation is not  * ambiguous during the transition away from DST.  *  *<p>This adapter is mutually compatible with the old adapter: the old adapter is able to read the  * UTC instant format, and this adapter can fall back to parsing the old format.  *  *<p>Older Gson versions are not able to parse milliseconds out of ISO 8601 instants, so this  * implementation truncates to seconds when writing. This is no worse than the truncation that  * happens to fit NoteDb timestamps into git commit formatting.  */
end_comment

begin_class
DECL|class|CommentTimestampAdapter
class|class
name|CommentTimestampAdapter
extends|extends
name|TypeAdapter
argument_list|<
name|Timestamp
argument_list|>
block|{
DECL|field|FALLBACK
specifier|private
specifier|static
specifier|final
name|DateTimeFormatter
name|FALLBACK
init|=
name|DateTimeFormatter
operator|.
name|ofLocalizedDateTime
argument_list|(
name|FormatStyle
operator|.
name|MEDIUM
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|write (JsonWriter out, Timestamp ts)
specifier|public
name|void
name|write
parameter_list|(
name|JsonWriter
name|out
parameter_list|,
name|Timestamp
name|ts
parameter_list|)
throws|throws
name|IOException
block|{
name|Timestamp
name|truncated
init|=
operator|new
name|Timestamp
argument_list|(
name|ts
operator|.
name|getTime
argument_list|()
operator|/
literal|1000
operator|*
literal|1000
argument_list|)
decl_stmt|;
name|out
operator|.
name|value
argument_list|(
name|ISO_INSTANT
operator|.
name|format
argument_list|(
name|truncated
operator|.
name|toInstant
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|read (JsonReader in)
specifier|public
name|Timestamp
name|read
parameter_list|(
name|JsonReader
name|in
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|str
init|=
name|in
operator|.
name|nextString
argument_list|()
decl_stmt|;
name|TemporalAccessor
name|ta
decl_stmt|;
try|try
block|{
name|ta
operator|=
name|ISO_INSTANT
operator|.
name|parse
argument_list|(
name|str
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DateTimeParseException
name|e
parameter_list|)
block|{
name|ta
operator|=
name|LocalDateTime
operator|.
name|from
argument_list|(
name|FALLBACK
operator|.
name|parse
argument_list|(
name|str
argument_list|)
argument_list|)
operator|.
name|atZone
argument_list|(
name|ZoneId
operator|.
name|systemDefault
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|Timestamp
operator|.
name|from
argument_list|(
name|Instant
operator|.
name|from
argument_list|(
name|ta
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

